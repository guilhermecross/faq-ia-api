package com.guilherme.faqiaapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PerguntaRequestDTO {

    @NotBlank(message = "A pergunta nao pode estar vazia")
    private String pergunta;
}
