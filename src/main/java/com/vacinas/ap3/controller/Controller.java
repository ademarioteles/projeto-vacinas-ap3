package com.vacinas.ap3.controller;

import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sanhok")
public class Controller {
    private RegistroDeVacinacaoService registroDeVacinacaoService;

    public Controller(RegistroDeVacinacaoService registroDeVacinacaoService) {
        this.registroDeVacinacaoService = registroDeVacinacaoService;
    }

    @GetMapping("")
    public ResponseEntity sanhok(){
        return ResponseEntity.status(200)
                .contentType(MediaType.TEXT_PLAIN)
                .body("API de Gerenciamento de Vacinação desenvolvida pela equipe Sanhok para atender aos requisitos do projeto 'Programação Web 2 - Oficial 2'");
    }

    @PostMapping("/inject")
    public ResponseEntity injectSanhok(){
        registroDeVacinacaoService.injetarDados();
        return ResponseEntity.status(200)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Registros de vacinação injetados");
    }



}