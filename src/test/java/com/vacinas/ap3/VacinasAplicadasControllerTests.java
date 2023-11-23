package com.vacinas.ap3;

import com.vacinas.ap3.controller.VacinasAplicadasController;
import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.entity.RegistroDeVacinacaoDoses;
import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import com.vacinas.ap3.util.RegistroDeVacinacaoUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VacinasAplicadasControllerTests {

    @InjectMocks
    private VacinasAplicadasController vacinasAplicadasControllerInject;

    @Mock
    private RegistroDeVacinacaoService registroDeVacinacaoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obterQuantidadeDeVacinacaoSemEstadoSucessoController() {
        Map<String, Object> resposta = new HashMap<>();
        List<RegistroDeVacinacao> registrosMock = RegistroDeVacinacaoUtils.criarOutraListaRegistrosExemploP1();
        resposta.put("O total de vacinas aplicadas é de", registrosMock.size());

        when(registroDeVacinacaoService.obterNumeroDeVacinacao(null)).thenReturn(resposta);

        ResponseEntity<Map<String, Object>> respostaEsperada = ResponseEntity.status(200).body(resposta);
        ResponseEntity<Map<String, Object>> respostaReal = vacinasAplicadasControllerInject.obterQuantidadeDeVacinacao(null);

        assertEquals(respostaEsperada, respostaReal);
    }

    @Test
    void obterQuantidadeDeVacinacaoComEstadoSucessoController() {
        Map<String, Object> resposta = new HashMap<>();
        List<RegistroDeVacinacao> registrosMock = RegistroDeVacinacaoUtils.criarOutraListaRegistrosExemploP1();
        resposta.put("O total de vacinas aplicadas é de", registrosMock.size());
        when(registroDeVacinacaoService.obterNumeroDeVacinacao("SP")).thenReturn(resposta);

        ResponseEntity<Map<String, Object>> respostaEsperada = ResponseEntity.status(200).body(resposta);
        ResponseEntity<Map<String, Object>> respostaReal = vacinasAplicadasControllerInject.obterQuantidadeDeVacinacao("SP");

        assertEquals(respostaEsperada, respostaReal);
    }

    @Test
    void obterDosesAplicadasSemFiltroSucessoController() {
        List<RegistroDeVacinacaoDoses> registros = Arrays.asList(
                new RegistroDeVacinacaoDoses("FabricanteExemplo", "VacinaExemplo", 2)
        );

        when(registroDeVacinacaoService.obterDosesAplicadas(null, null)).thenReturn(registros);

        ResponseEntity<List<RegistroDeVacinacaoDoses>> repostaEsperada = ResponseEntity.status(200)
                .body(Arrays.asList(
                        new RegistroDeVacinacaoDoses("FabricanteExemplo", "VacinaExemplo", 2)
                ));

        ResponseEntity<List<RegistroDeVacinacaoDoses>> respostaReal = vacinasAplicadasControllerInject.obterDosesAplicadas(null, null);

        assertEquals(repostaEsperada, respostaReal);
    }

    @Test
    void obterDosesAplicadasComFabricanteSucessoController() {
        List<RegistroDeVacinacaoDoses> registros = Arrays.asList(
                new RegistroDeVacinacaoDoses("FabricanteExemplo", "VacinaExemplo", 2)
        );

        when(registroDeVacinacaoService.obterDosesAplicadas(null, "Fabricante")).thenReturn(registros);

        ResponseEntity<List<RegistroDeVacinacaoDoses>> repostaEsperada = ResponseEntity.status(200)
                .body(Arrays.asList(
                        new RegistroDeVacinacaoDoses("FabricanteExemplo", "VacinaExemplo", 2)
                ));

        ResponseEntity<List<RegistroDeVacinacaoDoses>> respostaReal = vacinasAplicadasControllerInject.obterDosesAplicadas(null, "Fabricante");

        assertEquals(repostaEsperada, respostaReal);
    }

    @Test
    void obterDosesAplicadasComEstadoEFabricanteSucessoController() {
        List<RegistroDeVacinacaoDoses> registros = Arrays.asList(
                new RegistroDeVacinacaoDoses("FabricanteExemplo", "VacinaExemplo", 2)
        );

        when(registroDeVacinacaoService.obterDosesAplicadas("BA", "Fabricante")).thenReturn(registros);

        ResponseEntity<List<RegistroDeVacinacaoDoses>> repostaEsperada = ResponseEntity.status(200)
                .body(Arrays.asList(
                        new RegistroDeVacinacaoDoses("FabricanteExemplo", "VacinaExemplo", 2)
                ));

        ResponseEntity<List<RegistroDeVacinacaoDoses>> respostaReal = vacinasAplicadasControllerInject.obterDosesAplicadas("BA", "Fabricante");

        assertEquals(repostaEsperada, respostaReal);
    }

}
