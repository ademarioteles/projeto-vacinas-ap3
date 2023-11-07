package com.vacinas.ap3.controller;

import com.vacinas.ap3.DTO.Paciente;
import com.vacinas.ap3.entity.Mensagem;
import com.vacinas.ap3.entity.RegistroDeVacinacaoDoses;
import com.vacinas.ap3.entity.RegistroDeVacinacaoResumido;
import com.vacinas.ap3.exceptions.DataBaseException;
import com.vacinas.ap3.exceptions.ExteriorException;
import com.vacinas.ap3.exceptions.RegistroInexistenteException;
import com.vacinas.ap3.service.InterfaceAPI2Service;
import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class Pacientes {
    RegistroDeVacinacaoService registroDeVacinacaoService;
    private InterfaceAPI2Service interfaceAPI2Service;

    public Pacientes(RegistroDeVacinacaoService registroDeVacinacaoService, InterfaceAPI2Service interfaceAPI2Service) {
        this.registroDeVacinacaoService = registroDeVacinacaoService;
        this.interfaceAPI2Service = interfaceAPI2Service;
    }

    @GetMapping("/{id}/vacinas")
    public ResponseEntity<RegistroDeVacinacaoResumido> obterRegistroResumidoDeVacinacaoPorIdDoPaciente(@PathVariable String id) {
        try{
            RegistroDeVacinacaoResumido registroDeVacinacaoResumido = registroDeVacinacaoService.obterRegistroResumidoDeVacinacaoPorIdDoPaciente(id);
            if (registroDeVacinacaoResumido != null){
                return ResponseEntity.status(200).body(registroDeVacinacaoResumido);
            }else{
                throw new RegistroInexistenteException("Nenhum registro Encontrado");
            }
        } catch (DataAccessException | ExteriorException | DataBaseException | RegistroInexistenteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Mensagem(e.getMessage()));
        }
    }

    @GetMapping("/vacinas/atrasadas")
    public ResponseEntity<List<Paciente>> obterPacientesAtrasados(@RequestParam(name = "estado", required = false) String estado) {
        try{
            List<Paciente> pacientesAtrasados = registroDeVacinacaoService.obterPacientesAtrasados(estado);
            if (!pacientesAtrasados.isEmpty()){
                return ResponseEntity.status(200).body(pacientesAtrasados);
            }else{
                throw new RegistroInexistenteException("Nenhum paciente com doses atrasadas");
            }
        } catch (DataAccessException | ExteriorException | DataBaseException | RegistroInexistenteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Mensagem(e.getMessage()));
        }
   }
}
