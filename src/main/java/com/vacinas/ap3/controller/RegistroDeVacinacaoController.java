package com.vacinas.ap3.controller;

import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registros-de-vacinacao")

public class RegistroDeVacinacaoController {

    private RegistroDeVacinacaoService registroDeVacinacaoService;

    public RegistroDeVacinacaoController(RegistroDeVacinacaoService registroDeVacinacaoService) {
        this.registroDeVacinacaoService = registroDeVacinacaoService;
    }

    @PostMapping
    public RegistroDeVacinacao criarRegistroDeVacinacao(@RequestBody RegistroDeVacinacao registroDeVacinacao) {
        return registroDeVacinacaoService.criarRegistroDeVacinacao(registroDeVacinacao);
    }

    @GetMapping("/{id}")
    public RegistroDeVacinacao obterRegistroDeVacinaPorId(@PathVariable String id) {
        return registroDeVacinacaoService.obterRegistroDeVacinacaoPorId(id);
    }

}