package com.guilherme.faqiaapi.config;

import com.guilherme.faqiaapi.model.Produto;
import com.guilherme.faqiaapi.model.RegraAtendimento;
import com.guilherme.faqiaapi.repository.ProdutoRepository;
import com.guilherme.faqiaapi.repository.RegraAtendimentoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// CommandLineRunner roda automaticamente uma vez, assim que a
// aplicacao termina de subir. Usamos isso so para DEMONSTRACAO --
// popula o banco com alguns produtos e regras de exemplo, pra voce
// nao precisar cadastrar tudo na mao toda vez que reiniciar a API.
@Component
public class DataSeeder implements CommandLineRunner {

    private final ProdutoRepository produtoRepository;
    private final RegraAtendimentoRepository regraRepository;

    public DataSeeder(ProdutoRepository produtoRepository, RegraAtendimentoRepository regraRepository) {
        this.produtoRepository = produtoRepository;
        this.regraRepository = regraRepository;
    }

    @Override
    public void run(String... args) {
        produtoRepository.save(new Produto(null, "Tênis Runner", "Calçado esportivo",
                "Couro sintético e solado em borracha", "36, 37, 38, 39, 40",
                "Disponível nas cores preto e branco. Garantia de 90 dias contra defeito de fabricação."));

        produtoRepository.save(new Produto(null, "Sandália Verão", "Calçado casual",
                "Couro legítimo", "34, 35, 36, 37",
                "Recomendado não expor à água do mar por longos períodos."));

        regraRepository.save(new RegraAtendimento(null, "troca",
                "Trocas podem ser solicitadas em até 30 dias após o recebimento, desde que o produto não tenha sido usado."));

        regraRepository.save(new RegraAtendimento(null, "entrega",
                "O prazo de entrega é de 5 a 10 dias úteis, variando conforme a região."));

        regraRepository.save(new RegraAtendimento(null, "pagamento",
                "Aceitamos cartão de crédito em até 6x sem juros, PIX e boleto bancário."));
    }
}
