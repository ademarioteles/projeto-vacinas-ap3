package com.vacinas.ap3.controller;

import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pacientes")
public class Pacientes {
    RegistroDeVacinacaoService registroDeVacinacaoService;

    public Pacientes(RegistroDeVacinacaoService registroDeVacinacaoService) {
        this.registroDeVacinacaoService = registroDeVacinacaoService;
    }

    @GetMapping("/{id}/vacinas")
    public ResponseEntity<Object> obterRegistroResumidoDeVacinacaoPorIdDoPaciente(@PathVariable String id) {

        return ResponseEntity.status(200).body(registroDeVacinacaoService.obterRegistroResumidoDeVacinacaoPorIdDoPaciente(id));

    }
}
