package com.guilherme.faqiaapi.controller;

import com.guilherme.faqiaapi.dto.PerguntaRequestDTO;
import com.guilherme.faqiaapi.dto.RespostaDTO;
import com.guilherme.faqiaapi.model.Conversa;
import com.guilherme.faqiaapi.repository.ConversaRepository;
import com.guilherme.faqiaapi.service.FaqService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FaqController {

    private final FaqService faqService;
    private final ConversaRepository conversaRepository;

    public FaqController(FaqService faqService, ConversaRepository conversaRepository) {
        this.faqService = faqService;
        this.conversaRepository = conversaRepository;
    }

    @PostMapping("/perguntas")
    public RespostaDTO perguntar(@Valid @RequestBody PerguntaRequestDTO dto) {
        String resposta = faqService.responder(dto.getPergunta());
        return new RespostaDTO(resposta);
    }

    @GetMapping("/conversas")
    public List<Conversa> listarHistorico() {
        return conversaRepository.findAll();
    }
}
