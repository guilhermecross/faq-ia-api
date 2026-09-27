package com.guilherme.faqiaapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// politicas gerais (troca, entrega, pagamento) -- entra no prompt tambem
@Entity
@Table(name = "regra_atendimento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegraAtendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String topico; // ex: "troca", "entrega", "pagamento"

    @Column(length = 1000)
    @NotBlank
    private String descricao;
}