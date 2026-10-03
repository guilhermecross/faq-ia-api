package com.guilherme.faqiaapi.controller;

import com.guilherme.faqiaapi.model.RegraAtendimento;
import com.guilherme.faqiaapi.service.RegraAtendimentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regras")
public class RegraAtendimentoController {

    private final RegraAtendimentoService regraService;

    public RegraAtendimentoController(RegraAtendimentoService regraService) {
        this.regraService = regraService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegraAtendimento criar(@Valid @RequestBody RegraAtendimento regra) {
        return regraService.salvar(regra);
    }

    @GetMapping
    public List<RegraAtendimento> listar() {
        return regraService.listarTodas();
    }
}
