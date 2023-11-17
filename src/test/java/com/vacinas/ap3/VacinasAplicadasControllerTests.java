package com.vacinas.ap3;

import com.vacinas.ap3.controller.RegistroDeVacinacaoController;
import com.vacinas.ap3.controller.VacinasAplicadasController;
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
public class VacinasAplicadasControllerTests {

    @Autowired
    private VacinasAplicadasController vacinasAplicadasController;
    @InjectMocks
    private VacinasAplicadasController vacinasAplicadasControllerInject;

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
    void obterQuantidadeDeVacinacaoSemEstadoSucessoController() {
        List<RegistroDeVacinacao> registrosMock = Arrays.asList(
                RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo(),
                RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo()
        );

        when(registroDeVacinacaoService.obterNumeroDeVacinacao(null)).thenReturn(registrosMock.size());

        ResponseEntity<Integer> respostaEsperada = ResponseEntity.status(200).body(2);
        ResponseEntity<Integer> respostaReal = vacinasAplicadasControllerInject.obterQuantidadeDeVacinacao(null);

        assertEquals(respostaEsperada, respostaReal);
    }

    @Test
    void obterQuantidadeDeVacinacaoComEstadoSucessoController() {
        List<RegistroDeVacinacao> registrosMock = Arrays.asList(
                RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo(),
                RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo(),
                RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo()
        );

        when(registroDeVacinacaoService.obterNumeroDeVacinacao("SP")).thenReturn(registrosMock.size());

        ResponseEntity<Integer> respostaEsperada = ResponseEntity.status(200).body(3);
        ResponseEntity<Integer> respostaReal = vacinasAplicadasControllerInject.obterQuantidadeDeVacinacao("SP");

        assertEquals(respostaEsperada, respostaReal);
    }


}