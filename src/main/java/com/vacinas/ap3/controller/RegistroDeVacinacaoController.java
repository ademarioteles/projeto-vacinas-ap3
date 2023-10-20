package com.vacinas.ap3.controller;

import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registros-de-vacinacao")
public class RegistroDeVacinacaoController {
    private RegistroDeVacinacaoService registroDeVacinacaoService;

    public RegistroDeVacinacaoController(RegistroDeVacinacaoService registroDeVacinacaoService) {
        this.registroDeVacinacaoService = registroDeVacinacaoService;
    }

    @PostMapping
    public ResponseEntity criarRegistroDeVacinacao(@RequestBody RegistroDeVacinacao registroDeVacinacao) {
        return registroDeVacinacaoService.criarRegistroDeVacinacao(registroDeVacinacao);
    }

    @GetMapping("/paciente/{id}")
    public List<RegistroDeVacinacao> obterRegistroDeVacinacaoPorIdDoPaciente(@PathVariable String id) {
        return registroDeVacinacaoService.obterRegistroDeVacinacaoPorIdDoPaciente(id);
    }

    @GetMapping("/vacina/{id}")
    public List<RegistroDeVacinacao> obterRegistrosDeVacinacaoPorIdDaVacina(@PathVariable String id) {
        return registroDeVacinacaoService.obterRegistrosDeVacinacaoPorIdDaVacina(id);
    }

    @GetMapping("/lista")
    public List<RegistroDeVacinacao> obterListaRegistroDeVacinacao() {
        return registroDeVacinacaoService.listarTodosOsRegistrosDeVacinacao();
    }

}

