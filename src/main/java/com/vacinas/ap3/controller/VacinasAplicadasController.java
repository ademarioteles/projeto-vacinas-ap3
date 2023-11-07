package com.vacinas.ap3.controller;

import com.vacinas.ap3.entity.Mensagem;
import com.vacinas.ap3.entity.RegistroDeVacinacaoDoses;
import com.vacinas.ap3.exceptions.*;
import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/vacinas-aplicadas")
public class VacinasAplicadasController {
    RegistroDeVacinacaoService registroDeVacinacaoService;


    public VacinasAplicadasController(RegistroDeVacinacaoService registroDeVacinacaoService) {
        this.registroDeVacinacaoService = registroDeVacinacaoService;
    }

    @GetMapping("/quantidade")
    public ResponseEntity<Integer> obterQuantidadeDeVacinacao(@RequestParam(name = "estado", required = false) String estado) {
        try {
            return ResponseEntity.status(200).body(registroDeVacinacaoService.obterNumeroDeVacinacao(estado));
        } catch (ExteriorException |
                 DataBaseException e) {
            // Lidar com exceções
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(-1); // or any suitable default value
        }
    }

    @GetMapping("")
    public ResponseEntity<List<RegistroDeVacinacaoDoses>> obterDosesAplicadas(@RequestParam(name = "estado", required = false) String estado, @RequestParam(name = "fabricantes", required = false) String fabricantes) {
        try {
            return ResponseEntity.status(200).body(registroDeVacinacaoService.obterDosesAplicadas(estado, fabricantes));
        } catch (ExteriorException |
                 DataBaseException e) {
            // Lidar com exceções
            throw new DataBaseException("Erro ao listar registros de vacinação ");

        }
    }
}
