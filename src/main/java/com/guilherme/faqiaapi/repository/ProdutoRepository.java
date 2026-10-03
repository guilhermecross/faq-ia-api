package com.guilherme.faqiaapi.repository;

import com.guilherme.faqiaapi.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Busca "contendo" (LIKE) ignorando maiusculas/minusculas -- usado
    // pra achar o produto que o cliente esta perguntando, mesmo que ele
    // nao escreva o nome exato.
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    Optional<Produto> findFirstByNomeContainingIgnoreCase(String nome);
}
