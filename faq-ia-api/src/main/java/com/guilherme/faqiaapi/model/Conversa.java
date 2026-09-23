package com.guilherme.faqiaapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Guarda cada pergunta feita e a resposta gerada pela IA. Serve pra
// auditoria (ver se a IA respondeu bem) e pra montar um historico
// de atendimento.
@Entity
@Table(name = "conversa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conversa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String pergunta;

    @Column(length = 4000)
    private String resposta;

    private LocalDateTime dataHora = LocalDateTime.now();
}
