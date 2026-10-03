package com.guilherme.faqiaapi.service;

import com.guilherme.faqiaapi.dto.anthropic.AnthropicMessage;
import com.guilherme.faqiaapi.dto.anthropic.AnthropicRequest;
import com.guilherme.faqiaapi.dto.anthropic.AnthropicResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

// Responsavel SO por conversar com a API da Anthropic -- nao sabe
// nada sobre produtos, regras de negocio, etc. Isso separa bem as
// responsabilidades: se um dia trocarmos de provedor de IA, so essa
// classe muda.
@Service
public class AnthropicClientService {

    private final RestClient restClient;

    // A chave vem de uma VARIAVEL DE AMBIENTE (nunca do codigo-fonte).
    // Configurada em application.properties como ${ANTHROPIC_API_KEY}.
    @Value("${anthropic.api.key}")
    private String apiKey;

    @Value("${anthropic.api.model:claude-3-5-haiku-20241022}")
    private String model;

    public AnthropicClientService(RestClient anthropicRestClient) {
        this.restClient = anthropicRestClient;
    }

    public String gerarResposta(String systemPrompt, String perguntaDoUsuario) {
        AnthropicRequest requestBody = new AnthropicRequest(
                model,
                512, // max_tokens -- limite de tamanho da resposta gerada
                systemPrompt,
                List.of(new AnthropicMessage("user", perguntaDoUsuario))
        );

        AnthropicResponse response = restClient.post()
                .uri("/messages")
                .header("x-api-key", apiKey)
                .header("anthropic-version", "2023-06-01")
                .header("content-type", "application/json")
                .body(requestBody)
                .retrieve()
                .body(AnthropicResponse.class);

        if (response == null || response.getContent() == null || response.getContent().isEmpty()) {
            return "Desculpe, nao consegui gerar uma resposta no momento.";
        }

        return response.getContent().get(0).getText();
    }
}
