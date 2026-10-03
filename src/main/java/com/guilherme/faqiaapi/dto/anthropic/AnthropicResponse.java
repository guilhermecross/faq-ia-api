package com.guilherme.faqiaapi.dto.anthropic;

import lombok.Data;

import java.util.List;

// resposta da Anthropic -- o texto vem em content[0].text
@Data
public class AnthropicResponse {

    private List<ContentBlock> content;

    @Data
    public static class ContentBlock {
        private String type;
        private String text;
    }
}