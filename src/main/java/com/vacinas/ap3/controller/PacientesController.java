package com.vacinas.ap3.controller;

import com.vacinas.ap3.DTO.Paciente;
import com.vacinas.ap3.entity.RegistroDeVacinacaoResumido;
import com.vacinas.ap3.exceptions.RegistroInexistenteException;
import com.vacinas.ap3.service.ClientPacientesService;
import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacientesController {
    RegistroDeVacinacaoService registroDeVacinacaoService;
    private ClientPacientesService clientPacientesService;

    public PacientesController(RegistroDeVacinacaoService registroDeVacinacaoService, ClientPacientesService clientPacientesService) {
        this.registroDeVacinacaoService = registroDeVacinacaoService;
        this.clientPacientesService = clientPacientesService;
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