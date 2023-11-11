package com.vacinas.ap3;

import com.vacinas.ap3.DTO.Paciente;
import com.vacinas.ap3.DTO.Vacina;
import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.exceptions.*;
import com.vacinas.ap3.service.InterfaceAPI1Service;
import com.vacinas.ap3.service.InterfaceAPI2Service;
import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import com.vacinas.ap3.util.RegistroDeVacinacaoUtils;
import com.vacinas.ap3.util.VacinaUtils;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistroDeVacinacaoControllerTests {

    @Mock
    private InterfaceAPI2Service interfaceAPI2Service;
    @Mock
    private InterfaceAPI1Service interfaceAPI1Service;

    @InjectMocks
    private RegistroDeVacinacaoService registroDeVacinacaoService;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Testes ValidarPacienteExistente
    @Test
    void testValidarPacienteExistente_PacienteEncontrado() {
        String identificacaoDoPaciente = "identificacaoDoPaciente";
        Paciente pacienteRetorno = new Paciente(); // Suponhamos que você tenha uma classe Paciente

        // Mock da resposta da chamada externa simulando um paciente encontrado
        when(interfaceAPI2Service.PacienteDaApi2(identificacaoDoPaciente))
                .thenReturn(ResponseEntity.ok(pacienteRetorno));

        Paciente paciente = registroDeVacinacaoService.validarPacienteExistente(identificacaoDoPaciente);

        assertEquals(pacienteRetorno, paciente);
    }

    @Test
    void testValidarPacienteExistente_PacienteNaoEncontrado() {
        String identificacaoDoPaciente = "identificacaoDoPaciente";

        // Mock da resposta da chamada externa simulando paciente não encontrado
        when(interfaceAPI2Service.PacienteDaApi2(identificacaoDoPaciente))
                .thenReturn(ResponseEntity.notFound().build());

        ExteriorException exception = assertThrows(ExteriorException.class, () -> {
            registroDeVacinacaoService.validarPacienteExistente(identificacaoDoPaciente);
        });

        assertEquals("Paciente não encontrado na API externa", exception.getMessage());
    }

    @Test
    void testValidarPacienteExistente_ErroNaChamada() {
        String identificacaoDoPaciente = "identificacaoDoPaciente";

        // Mock do lançamento de uma exceção na chamada externa
        when(interfaceAPI2Service.PacienteDaApi2(identificacaoDoPaciente))
                .thenThrow(FeignException.class);

        ExteriorException exception = assertThrows(ExteriorException.class, () -> {
            registroDeVacinacaoService.validarPacienteExistente(identificacaoDoPaciente);
        });

        assertEquals("Erro ao buscar paciente na API externa", exception.getMessage());
    }

    // Teste ValidarVacinaExistente
    @Test
    void testValidarVacinaExistente_VacinaEncontrada() {
        Vacina vacinaRetorno = new Vacina(); // Simulação de uma vacina
        // Mock da resposta da chamada externa simulando a vacina encontrada
        when(interfaceAPI1Service.buscarVacinaDaApi1("identificacaoDaVacina"))
                .thenReturn(ResponseEntity.ok(vacinaRetorno));

        Vacina vacina = registroDeVacinacaoService.validarVacinaExistente("identificacaoDaVacina");

        assertEquals(vacinaRetorno, vacina);
    }

    @Test
    void testValidarVacinaExistente_VacinaNaoEncontrada() {
        // Mock da resposta da chamada externa simulando vacina não encontrada
        when(interfaceAPI1Service.buscarVacinaDaApi1("identificacaoDaVacina"))
                .thenReturn(ResponseEntity.notFound().build());

        ExteriorException exception = assertThrows(ExteriorException.class, () -> {
            registroDeVacinacaoService.validarVacinaExistente("identificacaoDaVacina");
        });

        assertEquals("Vacina não encontrado na API externa", exception.getMessage());
    }

    @Test
    void testValidarVacinaExistente_ErroNaChamada() {
        // Mock do lançamento de uma exceção na chamada externa
        when(interfaceAPI1Service.buscarVacinaDaApi1("identificacaoDaVacina"))
                .thenThrow(FeignException.class);

        ExteriorException exception = assertThrows(ExteriorException.class, () -> {
            registroDeVacinacaoService.validarVacinaExistente("identificacaoDaVacina");
        });

        assertEquals("Erro ao buscar Vacina na API externa", exception.getMessage());
    }

    // teste validarDose
    @Test
    void testValidarDose_ValidarPrimeiraDose() {
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();
        registro.setIdentificacaoDaDose(2);
        assertThrows(OrdemDoseInvalidaException.class, () -> registroDeVacinacaoService.validarPrimeiraDose(registro));
    }

    @Test
    void testValidarDose_ValidarDoseExistente() {
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();
        registro.setIdentificacaoDaDose(1);

        List<RegistroDeVacinacao> registros = new ArrayList<>();
        RegistroDeVacinacao registroExistente = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();
        registroExistente.setIdentificacaoDaDose(1);
        registros.add(registroExistente);

        assertThrows(RegistroExistenteException.class, () -> registroDeVacinacaoService.validarDoseExistente(registro, registros));
    }

    @Test
    void testValidarDose_ValidarIntervaloDoses() {
        List<RegistroDeVacinacao> registros = new ArrayList<>();
        LocalDate dataUltimaDose = LocalDate.now().minusDays(10);
        LocalDate dataRegistroAtual = LocalDate.now();

        Vacina vacinaExemplo = VacinaUtils.criarVacinaExemplo();

        when(interfaceAPI1Service.buscarVacinaDaApi1("identificacaoDaVacina"))
                .thenReturn(ResponseEntity.ok(vacinaExemplo));

        assertThrows(IntervaloInsuficienteException.class, () -> registroDeVacinacaoService.validarIntervaloDoses(dataUltimaDose, dataRegistroAtual, vacinaExemplo));
    }

    @Test
    void testValidarDose_ValidarOrdemDose() {
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();
        registro.setIdentificacaoDaDose(3);

        List<RegistroDeVacinacao> registros = new ArrayList<>();
        RegistroDeVacinacao registroAnterior = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();
        registroAnterior.setIdentificacaoDaDose(1);
        registros.add(registroAnterior);

        assertThrows(OrdemDoseInvalidaException.class, () -> registroDeVacinacaoService.validarOrdemDose(registro, registros));
    }

    @Test
    void testValidarDose_ValidarVacinaIncompativel() {
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();
        registro.setIdentificacaoDaVacina("vacina1");

        List<RegistroDeVacinacao> registros = new ArrayList<>();
        RegistroDeVacinacao registroAnterior = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();
        registroAnterior.setIdentificacaoDaVacina("vacina2");
        registros.add(registroAnterior);

        assertThrows(VacinaIncompativelException.class, () -> registroDeVacinacaoService.validarVacinaIncompativel(registro, registros));
    }

    // validarPrimeiraDose
    @Test
    void testValidarPrimeiraDose_IdentificacaoDose() {
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();
        // Verifica se nenhuma exceção é lançada quando a identificação é 1
        assertDoesNotThrow(() -> {
            registroDeVacinacaoService.validarPrimeiraDose(registro);
        });
    }

    @Test
    void testValidarPrimeiraDose_IdentificacaoDoseExcecao() {
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();
        registro.setIdentificacaoDaDose(2); // Configuração para simular uma identificação diferente de 1

        // Verifica se a exceção é lançada quando a identificação não é 1
        assertThrows(OrdemDoseInvalidaException.class, () -> {
            registroDeVacinacaoService.validarPrimeiraDose(registro);
        });
    }

    //validarDoseExistente
    @Test
    void testValidarDoseExistente_RegistroExistente() {
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();
        List<RegistroDeVacinacao> registros = new ArrayList<>();
        RegistroDeVacinacao registroExistente = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();
        registros.add(registroExistente);

        assertThrows(RegistroExistenteException.class, () -> {
            registroDeVacinacaoService.validarDoseExistente(registro, registros);
        });
    }

    @Test
    void testValidarDoseExistente_RegistroNaoExistente() {
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();
        registro.setIdentificacaoDaDose(2);

        List<RegistroDeVacinacao> registros = new ArrayList<>();

        assertDoesNotThrow(() -> {
            registroDeVacinacaoService.validarDoseExistente(registro, registros);
        });
    }

    //obterDataUltimaDose
    @Test
    void testObterDataUltimaDose() {
        List<RegistroDeVacinacao> registros = RegistroDeVacinacaoUtils.criarListaRegistrosExemplo();

        LocalDate dataUltimaDose = registroDeVacinacaoService.obterDataUltimaDose(registros);
        LocalDate expectedDate = LocalDate.now().plusDays(2); // Data esperada da última dose
        assertEquals(expectedDate, dataUltimaDose);
    }

    // obterUltimaDose
    @Test
    void testObterUltimaDose() {
        List<RegistroDeVacinacao> registros = RegistroDeVacinacaoUtils.criarListaRegistrosExemplo();

        RegistroDeVacinacao ultimaDose = registroDeVacinacaoService.obterUltimaDose(registros);
        assertEquals(3, ultimaDose.getIdentificacaoDaDose());
    }
    //confirmacaoUltimaDose
       /*
    @Test
 void testConfirmacaoUltimaDose_UltimaDoseConfirmada() {
        String idRegistro = "1"; // ID do registro para teste
        List<RegistroDeVacinacao> registros = RegistroDeVacinacaoUtils.criarListaRegistrosExemplo();
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();
        System.out.println(registro);
        RegistroDeVacinacaoService registroService = mock(RegistroDeVacinacaoService.class);
        when(registroService.obterRegistroDeVacinacaoPorId(idRegistro)).thenReturn(registro);
        when(registroService.obterRegistroDeVacinacaoPorIdDoPaciente(registro.getIdentificacaoDoPaciente())).thenReturn(registros);
        when(registroService.obterUltimaDose(registros)).thenReturn(registro);
        System.out.println(registro);
        assertTrue(registroService.confirmacaoUltimaDose(idRegistro));
    }

    @Test
    void testConfirmacaoUltimaDose_NaoConfirmada() {
        // Simulando um registro com doses anteriores, e tentando confirmar a última dose
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();
        registro.setIdentificacaoDaDose(1); // Define o registro atual como a primeira dose

        // Cria uma lista de registros com doses anteriores
        List<RegistroDeVacinacao> registrosAnteriores = RegistroDeVacinacaoUtils.criarListaRegistrosExemplo();

        assertFalse(registroDeVacinacaoService.confirmacaoUltimaDose(registro.getId()));
    }*/

    //validarIntervaloDoses
    @Test
    void testValidarIntervaloDoses_IntervaloSuficiente() {
        LocalDate dataUltimaDose = LocalDate.now().minusDays(21); // Data da última dose (exemplo: 3 semanas antes)
        LocalDate dataRegistroAtual = LocalDate.now(); // Data do registro atual
        Vacina vacinaAplicada = VacinaUtils.criarVacinaExemplo(); // Suponhamos uma vacina com intervalo de 21 dias

        // O intervalo entre as doses é suficiente (21 dias)
        assertDoesNotThrow(() -> registroDeVacinacaoService.validarIntervaloDoses(dataUltimaDose, dataRegistroAtual, vacinaAplicada));
    }

    @Test
    void testValidarIntervaloDoses_IntervaloInsuficiente() {
        LocalDate dataUltimaDose = LocalDate.now().minusDays(10); // Data da última dose (exemplo: 10 dias antes)
        LocalDate dataRegistroAtual = LocalDate.now(); // Data do registro atual
        Vacina vacinaAplicada = VacinaUtils.criarVacinaExemplo();

        // O intervalo entre as doses é insuficiente (menor que 21 dias)
        assertThrows(IntervaloInsuficienteException.class, () -> registroDeVacinacaoService.validarIntervaloDoses(dataUltimaDose, dataRegistroAtual, vacinaAplicada));
    }
    //validarOrdemDose
    @Test
    void testValidarOrdemDose_OrdemValida() {
        List<RegistroDeVacinacao> registros = RegistroDeVacinacaoUtils.criarListaRegistrosExemplo();
        RegistroDeVacinacao registro = new RegistroDeVacinacao();
        registro.setIdentificacaoDaDose(4);

        // Ordem de dose válida (sequência correta)
        assertDoesNotThrow(() -> registroDeVacinacaoService.validarOrdemDose(registro, registros));
    }
    @Test
    void testValidarOrdemDose_OrdemInvalida() {
        List<RegistroDeVacinacao> registros = RegistroDeVacinacaoUtils.criarListaRegistrosExemplo();
        RegistroDeVacinacao registro = new RegistroDeVacinacao();
        registro.setIdentificacaoDaDose(5);


        // Ordem de dose inválida (não segue a sequência)
        assertThrows(OrdemDoseInvalidaException.class, () -> registroDeVacinacaoService.validarOrdemDose(registro, registros));
    }

    //validarVacinaIncompativel
    @Test
    void testValidarVacinaIncompativel_VacinaCompativel() {

        List<RegistroDeVacinacao> registros = RegistroDeVacinacaoUtils.criarListaRegistrosExemplo();
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();

        // Vacina compatível com as doses anteriores
        assertDoesNotThrow(() -> registroDeVacinacaoService.validarVacinaIncompativel(registro, registros));
    }

    @Test
    void testValidarVacinaIncompativel_VacinaIncompativel() {
        List<RegistroDeVacinacao> registros = RegistroDeVacinacaoUtils.criarListaRegistrosExemplo();
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();
        registro.setIdentificacaoDaVacina("OutraVacina");
        // Vacina diferente das doses anteriores
        assertThrows(VacinaIncompativelException.class, () -> registroDeVacinacaoService.validarVacinaIncompativel(registro, registros));
    }
    //validarDataDeVacinacao
    @Test
    void testValidarDataDeVacinacao_DataPassada() {
        LocalDate dataNoPassado = LocalDate.now().minusDays(5);

        // A data no passado não deve lançar exceção
        assertDoesNotThrow(() -> registroDeVacinacaoService.validarDataDeVacinacao(dataNoPassado));
    }

    @Test
    void testValidarDataDeVacinacao_DataFutura() {
        LocalDate dataNoFuturo = LocalDate.now().plusDays(5);

        // A data no futuro deve lançar a exceção
        assertThrows(DataInvalidaException.class, () -> registroDeVacinacaoService.validarDataDeVacinacao(dataNoFuturo));
    }

    //listarTodosOsRegistrosDeVacinacao

}

