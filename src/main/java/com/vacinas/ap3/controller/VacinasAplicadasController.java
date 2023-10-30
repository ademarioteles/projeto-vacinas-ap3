package com.vacinas.ap3.controller;

import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vacinas-aplicadas")
public class VacinasAplicadasController {
    RegistroDeVacinacaoService registroDeVacinacaoService;


    public VacinasAplicadasController(RegistroDeVacinacaoService registroDeVacinacaoService) {
        this.registroDeVacinacaoService = registroDeVacinacaoService;
    }

    @GetMapping("")
    public ResponseEntity <Integer> obterQuantidadeDeVacinacao(@RequestParam(name = "estado", required = false) String estado) {
    return ResponseEntity.status(200).body(registroDeVacinacaoService.obterNumeroDeVacinacao(estado));
    }


}
