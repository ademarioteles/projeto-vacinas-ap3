package com.vacinas.ap3.controller;

import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pacientes")
public class Pacientes {
    RegistroDeVacinacaoService registroDeVacinacaoService;

    public Pacientes(RegistroDeVacinacaoService registroDeVacinacaoService) {
        this.registroDeVacinacaoService = registroDeVacinacaoService;
    }

    @GetMapping("/{id}/vacinas")
    public <Object> java.lang.Object obterRegistroResumidoDeVacinacaoPorIdDoPaciente(@PathVariable String id) {

        return registroDeVacinacaoService.obterRegistroResumidoDeVacinacaoPorIdDoPaciente(id);

    }
}
