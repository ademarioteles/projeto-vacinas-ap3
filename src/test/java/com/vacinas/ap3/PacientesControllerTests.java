package com.vacinas.ap3;

import com.vacinas.ap3.DTO.Paciente;
import com.vacinas.ap3.controller.PacientesController;
import com.vacinas.ap3.controller.VacinasAplicadasController;
import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.entity.RegistroDeVacinacaoDoses;
import com.vacinas.ap3.entity.RegistroDeVacinacaoResumido;
import com.vacinas.ap3.exceptions.RegistroInexistenteException;
import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import com.vacinas.ap3.util.PacienteUtils;
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
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PacientesControllerTests {

    @InjectMocks
    private PacientesController pacientesControllerInject;
    @Mock
    private RegistroDeVacinacaoService registroDeVacinacaoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void obterRegistroResumidoDeVacinacaoPorIdDoPacienteSucessoController() {
        RegistroDeVacinacaoResumido registro = new RegistroDeVacinacaoResumido();
        registro.setNome("Marcio");
        registro.setIdade(32);
        registro.setBairro("Centro");
        registro.setMunicipio("Lauro de Freitas");
        registro.setEstado("BA");
        registro.setFabricanteVacina("Pfizer");
        registro.setNomeVacina("Pfizer");
        registro.setTotalDeDosesVacina(3);
        registro.setIntervaloEntreDoses(21);

        when(registroDeVacinacaoService.obterRegistroResumidoDeVacinacaoPorIdDoPaciente("1")).thenReturn(registro);

        ResponseEntity<RegistroDeVacinacaoResumido> respostaEsperada = ResponseEntity.status(200).body(registro);
        ResponseEntity<RegistroDeVacinacaoResumido> respostaReal = pacientesControllerInject.obterRegistroResumidoDeVacinacaoPorIdDoPaciente("1");

        assertEquals(respostaEsperada, respostaReal);
    }
    @Test
    void obterRegistroResumidoDeVacinacaoPorIdDoPacienteErroController() {

        when(registroDeVacinacaoService.obterRegistroResumidoDeVacinacaoPorIdDoPaciente("1")).thenReturn(null);

        Assertions.assertThrows(RegistroInexistenteException.class, () -> pacientesControllerInject.obterRegistroResumidoDeVacinacaoPorIdDoPaciente("1"));

    }
    @Test
    void obterPacientesAtrasadosSucessoController() {
        List<Paciente> pacientes = PacienteUtils.criarListaPacientesExemplo();

        when(registroDeVacinacaoService.obterPacientesAtrasados("BA")).thenReturn(pacientes);
        ResponseEntity<List<Paciente>> respostaEsperada = ResponseEntity.status(200).body(pacientes);
        ResponseEntity<List<Paciente>> respostaReal = pacientesControllerInject.obterPacientesAtrasados("BA");
        assertEquals(respostaEsperada, respostaReal);
    }
    @Test
    void obterPacientesAtrasadosErroController() {
        List<Paciente> pacientes = new ArrayList<>();

        when(registroDeVacinacaoService.obterPacientesAtrasados("BA")).thenReturn(pacientes);
        Assertions.assertThrows(RegistroInexistenteException.class, () -> pacientesControllerInject.obterRegistroResumidoDeVacinacaoPorIdDoPaciente("1"));

    }
}