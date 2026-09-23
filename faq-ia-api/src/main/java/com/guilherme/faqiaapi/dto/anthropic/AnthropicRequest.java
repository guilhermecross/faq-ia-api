package com.guilherme.faqiaapi.dto.anthropic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Representa o "corpo" (body) que a API da Anthropic espera receber
// em POST https://api.anthropic.com/v1/messages
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnthropicRequest {

    private String model;

    @JsonProperty("max_tokens")
    private int maxTokens;

    private String system; // instrucoes de papel/contexto (engenharia de prompts)

    private List<AnthropicMessage> messages;
}
