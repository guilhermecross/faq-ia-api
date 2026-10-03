package com.guilherme.faqiaapi.service;

import com.guilherme.faqiaapi.model.Conversa;
import com.guilherme.faqiaapi.model.Produto;
import com.guilherme.faqiaapi.model.RegraAtendimento;
import com.guilherme.faqiaapi.repository.ConversaRepository;
import com.guilherme.faqiaapi.repository.ProdutoRepository;
import com.guilherme.faqiaapi.repository.RegraAtendimentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

        Conversa conversa = new Conversa();
        conversa.setPergunta(pergunta);
        conversa.setResposta(respostaGerada);
        conversaRepository.save(conversa);

        return respostaGerada;
    }

    private String montarSystemPrompt(String contexto) {
        return """
                Voce e um assistente de atendimento ao cliente de uma loja de calcados.

                Regras:
                1. Responda apenas com base no CONTEXTO abaixo.
                2. Se nao der pra responder com o contexto, diga que vai encaminhar pra um atendente humano. Nunca invente.
                3. Tom cordial, direto, profissional.
                4. Maximo 3 frases.
                5. Nao mencione que voce e uma IA.

                CONTEXTO:
                %s
                """.formatted(contexto);
    }

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

    private List<Produto> buscarProdutosMencionados(String pergunta) {
        String perguntaNormalizada = normalizar(pergunta);
        return produtoRepository.findAll().stream()
                .filter(p -> perguntaNormalizada.contains(normalizar(p.getNome())))
                .collect(Collectors.toList());
    }

    // ignora acentos pra nao perder o match (ex: "tenis" vs "tênis")
    private String normalizar(String texto) {
        String semAcento = java.text.Normalizer.normalize(texto, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcento.toLowerCase();
    }
}