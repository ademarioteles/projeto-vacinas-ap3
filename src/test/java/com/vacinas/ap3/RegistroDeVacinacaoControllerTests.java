package com.vacinas.ap3;


import com.vacinas.ap3.controller.RegistroDeVacinacaoController;
import com.vacinas.ap3.entity.Mensagem;
import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.exceptions.EditarException;
import com.vacinas.ap3.exceptions.RegistroInexistenteException;
import com.vacinas.ap3.service.InterfaceAPI1Service;
import com.vacinas.ap3.service.InterfaceAPI2Service;
import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import com.vacinas.ap3.util.RegistroDeVacinacaoUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolationException;

import java.time.LocalDate;
import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RegistroDeVacinacaoControllerTests {

    @Autowired
    private RegistroDeVacinacaoController registroDeVacinacaoController;

    @InjectMocks
    private RegistroDeVacinacaoController registroDeVacinacaoControllerInject;

    @Mock
    private RegistroDeVacinacaoService registroDeVacinacaoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        when(registroDeVacinacaoService.criarRegistroDeVacinacao(RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo())).thenReturn(true);
        when(registroDeVacinacaoService.editarRegistroDeVacinacao(RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo(), "1")).thenReturn(true);
        when(registroDeVacinacaoService.obterRegistroDeVacinacaoPorId("1")).thenReturn(RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo());

    }

    @Test
    void criarRegistroDeVacinacaoTest(){
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Mensagem("Registro cadastrado com sucesso!")), registroDeVacinacaoControllerInject.criarRegistroDeVacinacao
                (RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo()));
    }
    @Test
    void criarRegistroDeVacinacaoTestErrorController(){
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();
        registro.setIdentificacaoDaVacina("");
        Assertions.assertThrows(ConstraintViolationException.class, () -> registroDeVacinacaoController.criarRegistroDeVacinacao(registro));
    }
    @Test
    void editarRegistroDeVacinacaoTest(){
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Mensagem("Registro editado com sucesso!")), registroDeVacinacaoControllerInject.editarRegistroDeVacinacao
                (RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo(), "1"));
    }
    @Test
    void editarRegistroDeVacinacaoTestErrorController(){
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();
        registro.setIdentificacaoDaVacina("");
        Assertions.assertThrows(ConstraintViolationException.class, () -> registroDeVacinacaoController.editarRegistroDeVacinacao(registro, "1"));
    }
    @Test
    void editarRegistroDeVacinacaoFalhaAoEditarController() {
        // Simulando um registro existente
        RegistroDeVacinacao registroExistente = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();

        // Simulando uma chamada de edição mal-sucedida
        when(registroDeVacinacaoService.editarRegistroDeVacinacao(any(), eq("1"))).thenReturn(false);

        // Executando o método do controlador e capturando a exceção
        EditarException exception = assertThrows(EditarException.class,
                () -> registroDeVacinacaoControllerInject.editarRegistroDeVacinacao(registroExistente, "1"));

        // Verificando se a mensagem da exceção é a esperada
        assertEquals("Erro ao editar o registro", exception.getMessage());

        // Verificando se o serviço foi chamado corretamente
        verify(registroDeVacinacaoService, times(1)).editarRegistroDeVacinacao(any(), eq("1"));
    }

    @Test
    void editarRegistroDeVacinacaoNaoUltimaDoseController() {
        // Simulando um registro existente
        RegistroDeVacinacao registroExistente = new RegistroDeVacinacao();
        registroExistente.setId("2");
        registroExistente.setIdentificacaoDaDose(2);

        // Simulando uma chamada de edição mal-sucedida (não é a última dose)
        doThrow(new EditarException("Só é possível editar o último registro de vacinação"))
                .when(registroDeVacinacaoService).editarRegistroDeVacinacao(any(), eq("2"));

        // Executando o método do controlador e capturando a exceção
        EditarException exception = assertThrows(EditarException.class,
                () -> registroDeVacinacaoControllerInject.editarRegistroDeVacinacao(registroExistente, "2"));

        // Verificando se a mensagem da exceção é a esperada
        assertEquals("Só é possível editar o último registro de vacinação", exception.getMessage());

        // Verificando se o serviço foi chamado corretamente
        verify(registroDeVacinacaoService, times(1)).editarRegistroDeVacinacao(any(), eq("2"));
    }

    @Test
    void editarRegistroDeVacinacaoParcialSucessoController() {
        // Simulando um mapa de atualização
        Map<String, Object> atualizacao = new HashMap<>();
        atualizacao.put("dataDeVacinacao", LocalDate.now());

        // Simulando uma chamada de edição parcial bem-sucedida
        when(registroDeVacinacaoService.editarRegistroDeVacinacaoParcial(any(), any())).thenReturn(true);

        // Executando o método do controlador
        ResponseEntity responseEntity = registroDeVacinacaoControllerInject.editarRegistroDeVacinacaoParcial("1", atualizacao);

        // Verificando se o status e a mensagem são os esperados
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(new Mensagem("Registro editado com sucesso!"), responseEntity.getBody());

        // Verificando se o serviço foi chamado corretamente
        verify(registroDeVacinacaoService, times(1)).editarRegistroDeVacinacaoParcial(any(), any());
    }

    @Test
    void editarRegistroDeVacinacaoParcialFalhaAoEditarController() {
        // Simulando um mapa de atualização
        Map<String, Object> atualizacao = new HashMap<>();
        atualizacao.put("dataDeVacinacao", LocalDate.now());

        // Simulando uma chamada de edição parcial mal-sucedida
        when(registroDeVacinacaoService.editarRegistroDeVacinacaoParcial(any(), any())).thenReturn(false);

        // Executando o método do controlador e capturando a exceção
        EditarException exception = assertThrows(EditarException.class,
                () -> registroDeVacinacaoControllerInject.editarRegistroDeVacinacaoParcial("1", atualizacao));

        // Verificando se a mensagem da exceção é a esperada
        assertEquals("Erro ao editar o registro", exception.getMessage());

        // Verificando se o serviço foi chamado corretamente
        verify(registroDeVacinacaoService, times(1)).editarRegistroDeVacinacaoParcial(any(), any());
    }

    @Test
    void apagarRegistroDeVacinacaoPorIdSucessoController() {
        String registroId = "1";
        when(registroDeVacinacaoService.confirmacaoUltimaDose(registroId)).thenReturn(true);
        when(registroDeVacinacaoService.apagarRegistro(registroId)).thenReturn(true);
        Assertions.assertEquals(ResponseEntity.status(200)
                .body("Resgistro apagado com sucesso"), registroDeVacinacaoControllerInject.apagarRegistroDeVacinacaoPorId(registroId));
    }

    @Test
    void apagarRegistroDeVacinacaoPorIdFalhaController() {
        String registroId = "1";
        when(registroDeVacinacaoService.confirmacaoUltimaDose(registroId)).thenReturn(false);

        RegistroInexistenteException exception = assertThrows(RegistroInexistenteException.class,
                () -> registroDeVacinacaoControllerInject.apagarRegistroDeVacinacaoPorId(registroId));

        assertEquals("Nenhum registro Encontrado", exception.getMessage());
    }

    @Test
    void obterRegistroDeVacinacaoPorIdDoPacienteSucessoController() {
        String pacienteId = "1";
        List<RegistroDeVacinacao> registrosMock = Arrays.asList(
                RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo(),
                RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo()
        );

        when(registroDeVacinacaoService.obterRegistroDeVacinacaoPorIdDoPaciente(pacienteId)).thenReturn(registrosMock);

        ResponseEntity<List<RegistroDeVacinacao>> respostaEsperada = ResponseEntity.status(200).body(registrosMock);
        ResponseEntity<List<RegistroDeVacinacao>> respostaReal = registroDeVacinacaoControllerInject.obterRegistroDeVacinacaoPorIdDoPaciente(pacienteId);

        assertEquals(respostaEsperada, respostaReal);
    }

    @Test
    void obterRegistroDeVacinacaoPorIdDoPacienteFalhaController() {
        String pacienteId = "1";
        when(registroDeVacinacaoService.obterRegistroDeVacinacaoPorIdDoPaciente(pacienteId)).thenReturn(Collections.emptyList());

        RegistroInexistenteException exception = assertThrows(RegistroInexistenteException.class,
                () -> registroDeVacinacaoControllerInject.obterRegistroDeVacinacaoPorIdDoPaciente(pacienteId));

        assertEquals("Nenhum registro Encontrado", exception.getMessage());
    }

    @Test
    void obterRegistrosDeVacinacaoPorIdDaVacinaSucessoController() {
        String vacinaId = "1";
        List<RegistroDeVacinacao> registrosMock = Arrays.asList(
                RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo(),
                RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo()
        );

        when(registroDeVacinacaoService.obterRegistrosDeVacinacaoPorIdDaVacina(vacinaId)).thenReturn(registrosMock);

        ResponseEntity<List<RegistroDeVacinacao>> respostaEsperada = ResponseEntity.status(200).body(registrosMock);
        ResponseEntity<List<RegistroDeVacinacao>> respostaReal = registroDeVacinacaoControllerInject.obterRegistrosDeVacinacaoPorIdDaVacina(vacinaId);

        assertEquals(respostaEsperada, respostaReal);
    }

    @Test
    void obterRegistrosDeVacinacaoPorIdDaVacinaFalhaController() {
        String vacinaId = "1";
        when(registroDeVacinacaoService.obterRegistrosDeVacinacaoPorIdDaVacina(vacinaId)).thenReturn(Collections.emptyList());

        RegistroInexistenteException exception = assertThrows(RegistroInexistenteException.class,
                () -> registroDeVacinacaoControllerInject.obterRegistrosDeVacinacaoPorIdDaVacina(vacinaId));

        assertEquals("Nenhum registro Encontrado", exception.getMessage());
    }

    @Test
    void obterListaRegistroDeVacinacaoSucessoController() {
        List<RegistroDeVacinacao> registrosMock = Arrays.asList(
                RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo(),
                RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo()
        );

        when(registroDeVacinacaoService.listarTodosOsRegistrosDeVacinacao()).thenReturn(registrosMock);

        ResponseEntity<List<RegistroDeVacinacao>> respostaEsperada = ResponseEntity.status(200).body(registrosMock);
        ResponseEntity<List<RegistroDeVacinacao>> respostaReal = registroDeVacinacaoControllerInject.obterListaRegistroDeVacinacao();

        assertEquals(respostaEsperada, respostaReal);
    }

    @Test
    void obterListaRegistroDeVacinacaoFalhaController() {
        when(registroDeVacinacaoService.listarTodosOsRegistrosDeVacinacao()).thenReturn(Collections.emptyList());

        RegistroInexistenteException exception = assertThrows(RegistroInexistenteException.class,
                () -> registroDeVacinacaoControllerInject.obterListaRegistroDeVacinacao());

        assertEquals("Nenhum registro Encontrado", exception.getMessage());
    }

}