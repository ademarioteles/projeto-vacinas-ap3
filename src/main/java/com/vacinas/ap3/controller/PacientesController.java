package com.vacinas.ap3.controller;

import com.vacinas.ap3.DTO.Paciente;
import com.vacinas.ap3.entity.RegistroDeVacinacaoResumido;
import com.vacinas.ap3.exceptions.RegistroInexistenteException;
import com.vacinas.ap3.service.InterfaceAPI2Service;
import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacientesController {
    RegistroDeVacinacaoService registroDeVacinacaoService;
    private InterfaceAPI2Service interfaceAPI2Service;

    public PacientesController(RegistroDeVacinacaoService registroDeVacinacaoService, InterfaceAPI2Service interfaceAPI2Service) {
        this.registroDeVacinacaoService = registroDeVacinacaoService;
        this.interfaceAPI2Service = interfaceAPI2Service;
    }

    @GetMapping("/{id}/vacinas")
    public ResponseEntity<RegistroDeVacinacaoResumido> obterRegistroResumidoDeVacinacaoPorIdDoPaciente(@PathVariable String id) {
        RegistroDeVacinacaoResumido registroDeVacinacaoResumido = registroDeVacinacaoService.obterRegistroResumidoDeVacinacaoPorIdDoPaciente(id);
        if (registroDeVacinacaoResumido != null){
            return ResponseEntity.status(200).body(registroDeVacinacaoResumido);
        }else{
            throw new RegistroInexistenteException("Nenhum registro Encontrado");
        }
    }

    @GetMapping("/vacinas/atrasadas")
    public ResponseEntity<List<Paciente>> obterPacientesAtrasados(@RequestParam(name = "estado", required = false) String estado) {
        List<Paciente> pacientesAtrasados = registroDeVacinacaoService.obterPacientesAtrasados(estado);
        if (!pacientesAtrasados.isEmpty()){
            return ResponseEntity.status(200).body(pacientesAtrasados);
        }else{
            throw new RegistroInexistenteException("Nenhum paciente com doses atrasadas");
        }
    }
}