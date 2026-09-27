package com.guilherme.faqiaapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// contexto que a IA usa pra responder (sem isso ela inventa)
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