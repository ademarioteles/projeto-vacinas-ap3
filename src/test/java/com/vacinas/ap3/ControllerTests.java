package com.vacinas.ap3;

import com.vacinas.ap3.controller.Controller;
import com.vacinas.ap3.controller.VacinasAplicadasController;
import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.entity.RegistroDeVacinacaoDoses;
import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import com.vacinas.ap3.util.RegistroDeVacinacaoUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ControllerTests {

    @InjectMocks
    private Controller ControllerInject;



    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test

    void testeSanhok(){
        Assertions.assertEquals(ResponseEntity.status(200)
                .contentType(MediaType.TEXT_PLAIN)
                .body("API de Gerenciamento de Vacinação desenvolvida pela equipe Sanhok para atender aos requisitos do projeto 'Programação Web 2 - Oficial 2'"),ControllerInject.sanhok());

    }
}