package com.guilherme.faqiaapi.dto.anthropic;

import lombok.Data;

import java.util.List;

// Representa o que a API da Anthropic DEVOLVE. O texto gerado vem
// dentro de "content", que e uma lista de blocos (normalmente so
// um bloco do tipo "text" nas respostas simples).
@Data
public class AnthropicResponse {

    private List<ContentBlock> content;

    @Data
    public static class ContentBlock {
        private String type;
        private String text;
    }
}
