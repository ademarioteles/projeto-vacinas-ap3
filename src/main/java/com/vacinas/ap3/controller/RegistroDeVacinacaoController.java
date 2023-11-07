package com.vacinas.ap3.controller;

import com.vacinas.ap3.entity.Mensagem;
import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.exceptions.*;
import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/registros-de-vacinacao")
public class RegistroDeVacinacaoController {
    private RegistroDeVacinacaoService registroDeVacinacaoService;

    public RegistroDeVacinacaoController(RegistroDeVacinacaoService registroDeVacinacaoService) {
        this.registroDeVacinacaoService = registroDeVacinacaoService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity criarRegistroDeVacinacao(@RequestBody RegistroDeVacinacao registroDeVacinacao) {
        try {
            if (registroDeVacinacaoService.criarRegistroDeVacinacao(registroDeVacinacao) == true) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new Mensagem("Registro cadastrado com sucesso!"));
            } else {
                throw new ErroCriacaoRegistro("Erro ao criar o registro");
            }
        } catch (DataInvalidaException | ExteriorException | DataBaseException | OrdemDoseInvalidaException |
                 RegistroExistenteException | IntervaloInsuficienteException | OrdemDoseInvalidaException |
                 VacinaIncompativelException | ErroCriacaoRegistro | DataAccessException e) {
            // Lidar com exceções
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Mensagem(e.getMessage()));
        }
    }

    @PostMapping("/editar/{id}")
    public ResponseEntity editarRegistroDeVacinacao(@RequestBody RegistroDeVacinacao registroDeVacinacao, @PathVariable String id) {
        try {
            if (registroDeVacinacaoService.editarRegistroDeVacinacao(registroDeVacinacao, id) == true) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new Mensagem("Registro editado com sucesso!"));
            } else {
                throw new EditarException("Erro ao editar o registro");
            }
        } catch (RegistroInexistenteException | DataBaseException | ExteriorException | OrdemDoseInvalidaException |
                 RegistroExistenteException | IntervaloInsuficienteException | OrdemDoseInvalidaException |
                 VacinaIncompativelException | DataAccessException e) {
            // Lidar com exceções
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Mensagem(e.getMessage()));
        }
    }

    @PatchMapping("/editar/{id}")
    public ResponseEntity editarRegistroDeVacinacaoParcial(@PathVariable String id, @RequestBody Map<String, Object> atualizacao) {
        try {
            if (registroDeVacinacaoService.editarRegistroDeVacinacaoParcial(id, atualizacao) == true) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new Mensagem("Registro editado com sucesso!"));
            } else {
                throw new EditarException("Erro ao editar o registro");
            }
        } catch (RegistroInexistenteException | DataBaseException | ExteriorException | OrdemDoseInvalidaException |
                 RegistroExistenteException | IntervaloInsuficienteException | OrdemDoseInvalidaException |
                 VacinaIncompativelException | DataAccessException e) {
            // Lidar com exceções
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Mensagem(e.getMessage()));
        }

    }

    @GetMapping("/apagar/{id}")
    public ResponseEntity apagarRegistroDeVacinacaoPorId(@PathVariable String id) {
        try {
            if (registroDeVacinacaoService.apagarRegistro(id)) {
                return ResponseEntity.status(200).body("Resgistro apagado com sucesso");
            } else {
                throw new RegistroInexistenteException("Nenhum registro Encontrado");
            }
        } catch (DataAccessException | RegistroInexistenteException | DataBaseException | ApagarException e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Mensagem(e.getMessage()));
        }
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<RegistroDeVacinacao>> obterRegistroDeVacinacaoPorIdDoPaciente(@PathVariable String id) {
        try {
            if (!registroDeVacinacaoService.obterRegistroDeVacinacaoPorIdDoPaciente(id).isEmpty()) {
                return ResponseEntity.status(200).body(registroDeVacinacaoService.obterRegistroDeVacinacaoPorIdDoPaciente(id));
            } else {
                throw new RegistroInexistenteException("Nenhum registro Encontrado");
            }
        } catch (DataAccessException | RegistroInexistenteException | DataBaseException e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Mensagem(e.getMessage()));
        }
    }

    @GetMapping("/vacina/{id}")
    public ResponseEntity<List<RegistroDeVacinacao>> obterRegistrosDeVacinacaoPorIdDaVacina(@PathVariable String id) {
        try {
            if (!registroDeVacinacaoService.obterRegistrosDeVacinacaoPorIdDaVacina(id).isEmpty()) {
                return ResponseEntity.status(200).body(registroDeVacinacaoService.obterRegistrosDeVacinacaoPorIdDaVacina(id));
            } else {
                throw new RegistroInexistenteException("Nenhum registro Encontrado");
            }
        } catch (DataAccessException | RegistroInexistenteException | DataBaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Mensagem(e.getMessage()));
        }
    }

    @GetMapping("/lista")
    public ResponseEntity<List<RegistroDeVacinacao>> obterListaRegistroDeVacinacao() {
        try {
            if (!registroDeVacinacaoService.listarTodosOsRegistrosDeVacinacao().isEmpty()) {
                return ResponseEntity.status(200).body(registroDeVacinacaoService.listarTodosOsRegistrosDeVacinacao());
            } else {
                throw new RegistroInexistenteException("Nenhum registro Encontrado");
            }
        }catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Mensagem(e.getMessage()));
        }
    }

}

