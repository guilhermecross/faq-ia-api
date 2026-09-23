package com.guilherme.faqiaapi.dto.anthropic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnthropicMessage {
    private String role; // "user" ou "assistant"
    private String content;
}
