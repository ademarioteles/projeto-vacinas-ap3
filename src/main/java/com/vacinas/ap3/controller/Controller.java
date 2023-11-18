package com.vacinas.ap3.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class Controller {

    @GetMapping("/sanhok")
    public ResponseEntity sanhok(){
        return ResponseEntity.status(200)
                .contentType(MediaType.TEXT_PLAIN)
                .body("API de Gerenciamento de Vacinação desenvolvida pela equipe Sanhok para atender aos requisitos do projeto 'Programação Web 2 - Oficial 2'");
    }
}
