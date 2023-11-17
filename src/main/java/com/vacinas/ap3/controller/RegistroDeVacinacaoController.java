package com.vacinas.ap3.controller;

import com.vacinas.ap3.entity.Mensagem;
import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.exceptions.*;
import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import org.springframework.dao.DataAccessException;
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
    public ResponseEntity criarRegistroDeVacinacao(@RequestBody @Valid RegistroDeVacinacao registroDeVacinacao) {
            if (registroDeVacinacaoService.criarRegistroDeVacinacao(registroDeVacinacao)) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new Mensagem("Registro cadastrado com sucesso!"));
            } else {
                throw new ErroCriacaoRegistro("Erro ao criar o registro");
            }
    }

    @PostMapping("/editar/{id}")
    public ResponseEntity editarRegistroDeVacinacao(@RequestBody @Valid RegistroDeVacinacao registroDeVacinacao, @PathVariable String id) {
            if (registroDeVacinacaoService.editarRegistroDeVacinacao(registroDeVacinacao, id)) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new Mensagem("Registro editado com sucesso!"));
            } else {
                throw new EditarException("Erro ao editar o registro");
            }
    }

    @PatchMapping("/editar/{id}")
    public ResponseEntity editarRegistroDeVacinacaoParcial(@PathVariable String id, @RequestBody Map<String, Object> atualizacao) {
            if (registroDeVacinacaoService.editarRegistroDeVacinacaoParcial(id, atualizacao)) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new Mensagem("Registro editado com sucesso!"));
            } else {
                throw new EditarException("Erro ao editar o registro");
            }
    }

    @GetMapping("/apagar/{id}")
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

