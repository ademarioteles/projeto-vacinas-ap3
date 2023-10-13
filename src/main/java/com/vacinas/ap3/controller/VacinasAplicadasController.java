package com.vacinas.ap3.controller;

import com.vacinas.ap3.DTO.Paciente;
import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.service.InterfaceAPI2Service;
import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vacinas-aplicadas")
public class VacinasAplicadasController {
    RegistroDeVacinacaoService registroDeVacinacaoService;


    public VacinasAplicadasController(RegistroDeVacinacaoService registroDeVacinacaoService) {
        this.registroDeVacinacaoService = registroDeVacinacaoService;
    }

    @GetMapping("")
    public Integer obterQunatidadeDeVacinacao(@RequestParam(name = "estado", required = false) String estado) {
    return registroDeVacinacaoService.obterNumeroDeVacinacao(estado);
    }


}
