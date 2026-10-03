package com.guilherme.faqiaapi.repository;

import com.guilherme.faqiaapi.model.Conversa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversaRepository extends JpaRepository<Conversa, Long> {
}
