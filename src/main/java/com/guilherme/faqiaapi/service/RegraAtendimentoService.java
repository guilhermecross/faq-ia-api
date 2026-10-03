package com.guilherme.faqiaapi.service;

import com.guilherme.faqiaapi.model.RegraAtendimento;
import com.guilherme.faqiaapi.repository.RegraAtendimentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegraAtendimentoService {

    private final RegraAtendimentoRepository regraRepository;

    public RegraAtendimentoService(RegraAtendimentoRepository regraRepository) {
        this.regraRepository = regraRepository;
    }

    public RegraAtendimento salvar(RegraAtendimento regra) {
        return regraRepository.save(regra);
    }

    public List<RegraAtendimento> listarTodas() {
        return regraRepository.findAll();
    }
}
