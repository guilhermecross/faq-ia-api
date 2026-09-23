package com.guilherme.faqiaapi.service;

import com.guilherme.faqiaapi.model.Conversa;
import com.guilherme.faqiaapi.model.Produto;
import com.guilherme.faqiaapi.model.RegraAtendimento;
import com.guilherme.faqiaapi.repository.ConversaRepository;
import com.guilherme.faqiaapi.repository.ProdutoRepository;
import com.guilherme.faqiaapi.repository.RegraAtendimentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Aqui mora a ENGENHARIA DE PROMPTS de verdade: como transformar a
// pergunta do cliente + os dados do banco num prompt que faz a IA
// responder de forma precisa, no tom certo, e SEM INVENTAR informacao
// que nao esta no contexto (isso se chama "grounding").
@Service
public class FaqService {

    private final ProdutoRepository produtoRepository;
    private final RegraAtendimentoRepository regraRepository;
    private final ConversaRepository conversaRepository;
    private final AnthropicClientService anthropicClientService;

    public FaqService(ProdutoRepository produtoRepository,
                       RegraAtendimentoRepository regraRepository,
                       ConversaRepository conversaRepository,
                       AnthropicClientService anthropicClientService) {
        this.produtoRepository = produtoRepository;
        this.regraRepository = regraRepository;
        this.conversaRepository = conversaRepository;
        this.anthropicClientService = anthropicClientService;
    }

    public String responder(String pergunta) {
        String contexto = montarContexto(pergunta);
        String systemPrompt = montarSystemPrompt(contexto);

        String respostaGerada = anthropicClientService.gerarResposta(systemPrompt, pergunta);

        // Salva o historico, independente do resultado -- fundamental
        // pra rastreabilidade e pra revisar depois se a IA respondeu bem.
        Conversa conversa = new Conversa();
        conversa.setPergunta(pergunta);
        conversa.setResposta(respostaGerada);
        conversaRepository.save(conversa);

        return respostaGerada;
    }

    // O "system prompt" define o PAPEL da IA e as REGRAS de como ela
    // deve se comportar. E a parte mais importante da engenharia de
    // prompts: sem isso, a IA responde de forma generica e pode
    // inventar coisas que nao estao no seu catalogo.
    private String montarSystemPrompt(String contexto) {
        return """
                Voce e um assistente de atendimento ao cliente de uma loja de calcados.

                Regras que voce DEVE seguir:
                1. Responda SOMENTE com base nas informacoes fornecidas no CONTEXTO abaixo.
                2. Se a pergunta nao puder ser respondida com o contexto disponivel, diga
                   educadamente que vai encaminhar a duvida para um atendente humano -- NUNCA invente informacao.
                3. Use um tom cordial, direto e profissional.
                4. Respostas devem ter no maximo 3 frases.
                5. Nao mencione que voce e uma IA nem explique como voce funciona.

                CONTEXTO DISPONIVEL:
                %s
                """.formatted(contexto);
    }

    // Busca no banco so o que e relevante pra pergunta -- em vez de
    // mandar o catalogo inteiro pra IA toda vez (o que custaria mais
    // tokens e poderia confundir o modelo).
    private String montarContexto(String pergunta) {
        StringBuilder contexto = new StringBuilder();

        List<Produto> produtosRelevantes = buscarProdutosMencionados(pergunta);
        if (!produtosRelevantes.isEmpty()) {
            contexto.append("Produtos:\n");
            produtosRelevantes.forEach(p -> contexto.append("- %s | Categoria: %s | Material: %s | Tamanhos: %s | Obs: %s\n"
                    .formatted(p.getNome(), p.getCategoria(), p.getMaterial(),
                            p.getTamanhosDisponiveis(), p.getObservacoes())));
        }

        List<RegraAtendimento> regras = regraRepository.findAll();
        if (!regras.isEmpty()) {
            contexto.append("\nPoliticas da loja:\n");
            regras.forEach(r -> contexto.append("- %s: %s\n".formatted(r.getTopico(), r.getDescricao())));
        }

        if (contexto.isEmpty()) {
            return "Nenhuma informacao cadastrada ainda.";
        }

        return contexto.toString();
    }

    // Estrategia simples: procura, entre os produtos cadastrados, se
    // o nome de algum aparece mencionado na pergunta. Uma versao mais
    // avancada (fora do escopo deste projeto) usaria busca semantica.
    private List<Produto> buscarProdutosMencionados(String pergunta) {
        String perguntaNormalizada = normalizar(pergunta);
        return produtoRepository.findAll().stream()
                .filter(p -> perguntaNormalizada.contains(normalizar(p.getNome())))
                .collect(Collectors.toList());
    }

    private String normalizar(String texto) {
        String semAcento = java.text.Normalizer.normalize(texto, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcento.toLowerCase();
    }
}
