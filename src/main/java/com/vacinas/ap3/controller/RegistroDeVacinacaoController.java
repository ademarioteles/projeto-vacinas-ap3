package com.vacinas.ap3.controller;

import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.exceptions.*;
import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/registros-de-vacinacao")
@Validated
public class RegistroDeVacinacaoController {
    private RegistroDeVacinacaoService registroDeVacinacaoService;

    public RegistroDeVacinacaoController(RegistroDeVacinacaoService registroDeVacinacaoService) {
        this.registroDeVacinacaoService = registroDeVacinacaoService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity <RegistroDeVacinacao> criarRegistroDeVacinacao(@RequestBody @Valid RegistroDeVacinacao registroDeVacinacao) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(registroDeVacinacaoService.criarRegistroDeVacinacao(registroDeVacinacao));
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<RegistroDeVacinacao> editarRegistroDeVacinacao(@RequestBody @Valid RegistroDeVacinacao registroDeVacinacao, @PathVariable String id) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(registroDeVacinacaoService.editarRegistroDeVacinacao(registroDeVacinacao, id));
    }

    @PatchMapping("/editar/{id}")
    public ResponseEntity editarRegistroDeVacinacaoParcial(@PathVariable String id, @RequestBody Map<String, Object> atualizacao) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(registroDeVacinacaoService.editarRegistroDeVacinacaoParcial(id, atualizacao));
    }

    @DeleteMapping("/apagar/{id}")
    public ResponseEntity apagarRegistroDeVacinacaoPorId(@PathVariable String id) {
        if (registroDeVacinacaoService.apagarRegistro(id)) {
            return ResponseEntity.status(200).body("Resgistro apagado com sucesso");
        } else {
            throw new RegistroInexistenteException("Nenhum registro Encontrado");
        }
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<RegistroDeVacinacao>> obterRegistroDeVacinacaoPorIdDoPaciente(@PathVariable String id) {
        if (!registroDeVacinacaoService.obterRegistroDeVacinacaoPorIdDoPaciente(id).isEmpty()) {
            return ResponseEntity.status(200).body(registroDeVacinacaoService.obterRegistroDeVacinacaoPorIdDoPaciente(id));
        } else {
            throw new RegistroInexistenteException("Nenhum registro Encontrado");
        }
    }

    @GetMapping("/vacina/{id}")
    public ResponseEntity<List<RegistroDeVacinacao>> obterRegistrosDeVacinacaoPorIdDaVacina(@PathVariable String id) {
        if (!registroDeVacinacaoService.obterRegistrosDeVacinacaoPorIdDaVacina(id).isEmpty()) {
            return ResponseEntity.status(200).body(registroDeVacinacaoService.obterRegistrosDeVacinacaoPorIdDaVacina(id));
        } else {
            throw new RegistroInexistenteException("Nenhum registro Encontrado");
        }
    }

    @GetMapping("/lista")
    public ResponseEntity<List<RegistroDeVacinacao>> obterListaRegistroDeVacinacao() {
        if (!registroDeVacinacaoService.listarTodosOsRegistrosDeVacinacao().isEmpty()) {
            return ResponseEntity.status(200).body(registroDeVacinacaoService.listarTodosOsRegistrosDeVacinacao());
        } else {
            throw new RegistroInexistenteException("Nenhum registro Encontrado");
        }
    }

}