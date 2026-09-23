package com.guilherme.faqiaapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Base de conhecimento que a IA vai USAR PARA RESPONDER -- e o que
// chamamos de "contexto" na engenharia de prompts. Sem isso, a IA
// inventaria informacao (alucinacao); com isso, ela responde so com
// o que esta cadastrado aqui.
@Entity
@Table(name = "produto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String categoria;

    @NotBlank
    private String material;

    private String tamanhosDisponiveis; // ex: "36, 37, 38, 39, 40"

    @Column(length = 1000)
    private String observacoes; // detalhes extras (cuidados, garantia, etc.)
}
