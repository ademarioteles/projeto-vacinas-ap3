package com.vacinas.ap3;

import com.vacinas.ap3.DTO.Paciente;
import com.vacinas.ap3.DTO.Vacina;
import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.entity.RegistroDeVacinacaoDoses;
import com.vacinas.ap3.exceptions.*;
import com.vacinas.ap3.repository.RegistroDeVacinacaoRepository;
import com.vacinas.ap3.service.InterfaceAPI1Service;
import com.vacinas.ap3.service.InterfaceAPI2Service;
import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import com.vacinas.ap3.util.PacienteUtils;
import com.vacinas.ap3.util.RegistroDeVacinacaoUtils;
import com.vacinas.ap3.util.VacinaUtils;
import feign.FeignException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistroDeVacinacaoControllerTests {

    @Mock
    private InterfaceAPI2Service interfaceAPI2Service;
    @Mock
    private InterfaceAPI1Service interfaceAPI1Service;

    @InjectMocks
    private RegistroDeVacinacaoService registroDeVacinacaoService;
    @Mock
    private RegistroDeVacinacaoRepository registroDeVacinacaoRepository;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Before
    void init(){
        registroDeVacinacaoRepository = mock(RegistroDeVacinacaoRepository.class);
        registroDeVacinacaoService = new RegistroDeVacinacaoService(registroDeVacinacaoRepository, interfaceAPI2Service, interfaceAPI1Service);
    }


    // Testes ValidarPacienteExistente
    @Test
    void testValidarPacienteExistente_PacienteEncontrado() {
        String identificacaoDoPaciente = "identificacaoDoPaciente";
        Paciente pacienteRetorno = PacienteUtils.criarUmPaciente(); // Suponhamos que você tenha uma classe Paciente

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

    @Test
    void testConfirmacaoUltimaDose_UltimaDoseConfirmada() {
        String idRegistro = "1";
        List<RegistroDeVacinacao> registros = RegistroDeVacinacaoUtils.criarListaRegistrosExemplo();
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();



        // Simula o comportamento do repositório ao chamar o método findById
        when(registroDeVacinacaoRepository.findById(idRegistro)).thenReturn(Optional.of(Optional.of(registro).get()));
        when(registroDeVacinacaoService.obterRegistroDeVacinacaoPorId(idRegistro)).thenReturn(registro);
        when(registroDeVacinacaoService.obterRegistroDeVacinacaoPorIdDoPaciente(registro.getIdentificacaoDoPaciente())).thenReturn(registros);
        when(registroDeVacinacaoService.obterUltimaDose(registros)).thenReturn(registro);

        assertTrue(registroDeVacinacaoService.confirmacaoUltimaDose(idRegistro));
    }

    @Test
    void testConfirmacaoUltimaDose_NaoConfirmada() {
        // Simulando um registro com doses anteriores, e tentando confirmar a última dose
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();
        registro.setIdentificacaoDaDose(1); // Define o registro atual como a primeira dose

        // Cria uma lista de registros com doses anteriores
        List<RegistroDeVacinacao> registrosAnteriores = RegistroDeVacinacaoUtils.criarListaRegistrosExemplo();

        assertFalse(registroDeVacinacaoService.confirmacaoUltimaDose(registro.getId()));
    }

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
    @Test
    void testListarTodosOsRegistrosDeVacinacao_Sucesso() {
        // Criação de uma lista de registros fictícia
        List<RegistroDeVacinacao> registrosFicticios = RegistroDeVacinacaoUtils.criarListaRegistrosExemplo();

        when(registroDeVacinacaoRepository.findAll()).thenReturn(registrosFicticios);

        // Verifica se a lista é retornada corretamente
        List<RegistroDeVacinacao> resultado = registroDeVacinacaoService.listarTodosOsRegistrosDeVacinacao();
        assertEquals(registrosFicticios.size(), resultado.size());
    }

    @Test
    void testListarTodosOsRegistrosDeVacinacao_SemRegistros() {
        // Simula o comportamento do repositório retornando uma lista vazia
        when(registroDeVacinacaoRepository.findAll()).thenReturn(Collections.emptyList());

        // Verifica se uma exceção é lançada ao tentar buscar registros quando a lista está vazia
        assertThrows(DataBaseException.class, () -> registroDeVacinacaoService.listarTodosOsRegistrosDeVacinacao());
    }

    @Test
    void testListarTodosOsRegistrosDeVacinacao_ErroBancoDeDados() {
        // Simula o repositório lançando uma exceção
        when(registroDeVacinacaoRepository.findAll()).thenThrow(new DataAccessResourceFailureException("Erro ao acessar o banco de dados"));


        // Verifica se uma exceção é lançada quando ocorre um erro no acesso ao banco de dados
        assertThrows(DataAccessResourceFailureException.class, () -> registroDeVacinacaoService.listarTodosOsRegistrosDeVacinacao());
    }

    //obterRegistrosDeVacinacaoPorIdDaVacina
    @Test
    void testObterRegistrosDeVacinacaoPorIdDaVacina() {
        // Criação de um ID de vacina para teste
        String idVacina = "652f344fe8be16628ceb8f0b";

        List<RegistroDeVacinacao> registros = RegistroDeVacinacaoUtils.criarListaRegistrosExemplo();

        // Simula o comportamento do repositório ao chamar o método findByIdentificacaoDaVacina
        when(registroDeVacinacaoRepository.findByIdentificacaoDaVacina(idVacina)).thenReturn(registros);


        // Chama o método para buscar registros de vacinação por ID da vacina
        List<RegistroDeVacinacao> registrosEncontrados = registroDeVacinacaoService.obterRegistrosDeVacinacaoPorIdDaVacina(idVacina);

        // Verifica se o resultado retornado corresponde aos registros simulados
        assertEquals(3, registrosEncontrados.size());
    }

    //obterRegistroDeVacinacaoPorId
    @Test
    void testObterRegistroDeVacinacaoPorId_RegistroEncontrado() {
        // ID do registro para teste
        String idRegistro = "1";

        // Criação de um registro de vacinação simulado
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();

        // Simula o comportamento do repositório ao chamar o método findById
        when(registroDeVacinacaoRepository.findById(idRegistro)).thenReturn(Optional.of(registro));


        // Chama o método para obter um registro de vacinação por ID
        RegistroDeVacinacao registroEncontrado = registroDeVacinacaoService.obterRegistroDeVacinacaoPorId(idRegistro);

        // Verifica se o registro retornado corresponde ao simulado
        assertEquals(idRegistro, registroEncontrado.getId());
    }

    @Test
    void testObterRegistroDeVacinacaoPorId_RegistroNaoEncontrado() {
        // ID de um registro que não existe no repositório
        String idRegistroNaoExistente = "registro_inexistente";

        // Simula o comportamento do repositório retornando um Optional vazio
        when(registroDeVacinacaoRepository.findById(idRegistroNaoExistente)).thenReturn(Optional.empty());


        // Verifica se ao tentar obter o registro não existente, a exceção é lançada
        RegistroInexistenteException exception = assertThrows(RegistroInexistenteException.class, () -> {
            registroDeVacinacaoService.obterRegistroDeVacinacaoPorId(idRegistroNaoExistente);
        });

        // Verifica se a mensagem da exceção corresponde à mensagem esperada
        assertEquals("Registro não encontrado", exception.getMessage());
    }

    // calculoIdade
    @Test
    void testCalculoIdade() {
        // Dados para o teste
        String dataNascimento = "1990-01-01";
        LocalDate dataAtual = LocalDate.of(2023, 1, 1); // Simula o dia de hoje

        // Chama o método de cálculo de idade
        Integer idadeCalculada = registroDeVacinacaoService.calculoIdade(dataNascimento);

        // Calcula a idade real
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dataNasc = LocalDate.parse(dataNascimento, formatter);
        int idadeEsperada = dataAtual.getYear() - dataNasc.getYear();

        // Verifica se as idades coincidem
        assertEquals(idadeEsperada, idadeCalculada);
    }
    //obterNumeroDeVacinacao
   /* @Test
    void testObterNumeroDeVacinacao() {
        // Simulando estado de teste
        String estado = "SP";

        // Mock para lista de registros de vacinação
        List<RegistroDeVacinacao> registros = RegistroDeVacinacaoUtils.criarListaRegistrosExemplo();
        // Mock para interface de API externa
        Paciente paciente1 = PacienteUtils.criarUmPaciente();
        ResponseEntity<Paciente> responseEntity1 = new ResponseEntity<>(paciente1, HttpStatus.OK);
        when(interfaceAPI2Service.PacienteDaApi2("1")).thenReturn(responseEntity1);

        Paciente paciente2 = PacienteUtils.criarOutroPaciente();
        System.out.println(registros);
        ResponseEntity<Paciente> responseEntity2 = new ResponseEntity<>(paciente2, HttpStatus.OK);
        when(interfaceAPI2Service.PacienteDaApi2("2")).thenReturn(responseEntity2);
        when(registroDeVacinacaoService.listarTodosOsRegistrosDeVacinacao()).thenReturn(registros);

        // Teste do método obterNumeroDeVacinacao
        int numVacinacoes = registroDeVacinacaoService.obterNumeroDeVacinacao(estado);
        assertEquals(1, numVacinacoes); // Apenas um paciente está em SP na lista
    }*/

    //obterEstadoDoPaciente
    @Test
    void testObterEstadoDoPaciente_PacienteEncontrado() {
        String identificacaoDoPaciente = "1";
        Paciente pacienteRetorno = PacienteUtils.criarUmPaciente(); // Suponhamos que você tenha uma classe Paciente

        // Mock da resposta da chamada externa simulando um paciente encontrado
        when(interfaceAPI2Service.PacienteDaApi2(identificacaoDoPaciente))
                .thenReturn(ResponseEntity.ok(pacienteRetorno));

        String estado = registroDeVacinacaoService.obterEstadoDoPaciente("1");
        assertEquals("SP", estado);
    }

    @Test
    void testObterEstadoDoPaciente_PacienteNaoEncontrado() {
        // Configuração do mock com paciente não encontrado na API externa
        String idPaciente = "1";
        ResponseEntity<Paciente> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(interfaceAPI2Service.PacienteDaApi2(idPaciente)).thenReturn(responseEntity);

        assertThrows(ExteriorException.class, () -> registroDeVacinacaoService.obterEstadoDoPaciente("1"));
    }

    //obterDosesAplicadas
    @Test
    void testObterDosesAplicadas() {

        // Simulando a lista de registros de vacinação
        List<RegistroDeVacinacao> registros = RegistroDeVacinacaoUtils.criarOutraListaRegistrosExemploP1();
        Paciente pacienteRetorno = PacienteUtils.criarUmPaciente(); // Suponhamos que você tenha uma classe Paciente

        // Mock da resposta da chamada externa simulando um paciente encontrado
        when(interfaceAPI2Service.PacienteDaApi2("1"))
                .thenReturn(ResponseEntity.ok(pacienteRetorno));
        // Criação da sua classe e injeção do repositório simulado
    when(registroDeVacinacaoRepository.findAll()).thenReturn(registros);
        // Simulando a resposta de busca da vacina
        Vacina vacina = VacinaUtils.criarVacinaExemplo();
        ResponseEntity<Vacina> responseEntityVacina = new ResponseEntity<>(vacina, HttpStatus.OK);
        when(registroDeVacinacaoService.interfaceAPI1Service.buscarVacinaDaApi1("1")).thenReturn(responseEntityVacina);


        // Teste do método obterDosesAplicadas
        List<RegistroDeVacinacaoDoses> resultado = registroDeVacinacaoService.obterDosesAplicadas(null, "FabricanteY");
        System.out.println(resultado);
        // Verifique se o resultado possui o tamanho esperado ou outra lógica
        assertEquals(3, resultado.get(0).getDosesAplicadas());
    }

    @Test
    void testObterDosesAplicadas_Estado() {

        // Simulando a lista de registros de vacinação
        List<RegistroDeVacinacao> registros = RegistroDeVacinacaoUtils.criarOutraListaRegistrosExemploP2();
        Paciente pacienteRetorno = PacienteUtils.criarOutroPaciente(); // Suponhamos que você tenha uma classe Paciente

        // Mock da resposta da chamada externa simulando um paciente encontrado
        when(interfaceAPI2Service.PacienteDaApi2("2"))
                .thenReturn(ResponseEntity.ok(pacienteRetorno));
        // Criação da sua classe e injeção do repositório simulado
         when(registroDeVacinacaoRepository.findAll()).thenReturn(registros);
        // Simulando a resposta de busca da vacina
        Vacina vacina = VacinaUtils.criarVacinaExemplo();
        ResponseEntity<Vacina> responseEntityVacina = new ResponseEntity<>(vacina, HttpStatus.OK);
        when(registroDeVacinacaoService.interfaceAPI1Service.buscarVacinaDaApi1("1")).thenReturn(responseEntityVacina);


        // Teste do método obterDosesAplicadas
        List<RegistroDeVacinacaoDoses> resultado = registroDeVacinacaoService.obterDosesAplicadas("RJ", "FabricanteY");
        System.out.println(resultado);
        // Verifique se o resultado possui o tamanho esperado ou outra lógica
        assertEquals(2, resultado.get(0).getDosesAplicadas());
    }

    @Test
    void testObterDosesAplicadas_Vazio() {

        // Simulando a lista de registros de vacinação
        List<RegistroDeVacinacao> registros = new ArrayList<>();
        RegistroDeVacinacaoRepository registroDeVacinacaoRepository = mock(RegistroDeVacinacaoRepository.class);
        Paciente pacienteRetorno = PacienteUtils.criarOutroPaciente(); // Suponhamos que você tenha uma classe Paciente

        // Mock da resposta da chamada externa simulando um paciente encontrado
        when(interfaceAPI2Service.PacienteDaApi2("2"))
                .thenReturn(ResponseEntity.ok(pacienteRetorno));
        // Criação da sua classe e injeção do repositório simulado
        when(registroDeVacinacaoRepository.findAll()).thenReturn(registros);
        // Simulando a resposta de busca da vacina
        Vacina vacina = VacinaUtils.criarVacinaExemplo();
        ResponseEntity<Vacina> responseEntityVacina = new ResponseEntity<>(vacina, HttpStatus.OK);
        when(registroDeVacinacaoService.interfaceAPI1Service.buscarVacinaDaApi1("1")).thenReturn(responseEntityVacina);


        // Verifique se o resultado possui o tamanho esperado ou outra lógica
        assertThrows(DataBaseException.class, () -> registroDeVacinacaoService.obterDosesAplicadas("RJ", "FabricanteY"));
    }
//salvarRegistroEditado

    @Test
    public void testSalvarRegistroEditado() {

        // Criando um objeto de registro para testar
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();

        // Criando uma instância da classe que contém o método a ser testado

        // Chamar o método que queremos testar
        registroDeVacinacaoService.salvarRegistroEditado(registro);

        // Verificando se o método save foi chamado no registroDeVacinacaoRepository
        verify(registroDeVacinacaoRepository, times(1)).save(registro);
    }
    /*
    // apagarRegistro
    @Test
    public void testApagarRegistroUltimaDoseConfirmada() {
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();
        // Criando um mock para registroDeVacinacaoRepository
        RegistroDeVacinacaoRepository registroDeVacinacaoRepository = mock(RegistroDeVacinacaoRepository.class);

        // Configurando o mock para confirmar a última dose
        when(registroDeVacinacaoService.confirmacaoUltimaDose("ID_VALIDO")).thenReturn(true);
        when(registroDeVacinacaoRepository.findById("ID_VALIDO")).thenReturn(Optional.of(registro));


        // Chamar o método que queremos testar
        Boolean resultado = registroDeVacinacaoService.apagarRegistro("ID_VALIDO");

        // Verificando se o método deleteById foi chamado no registroDeVacinacaoRepository
        verify(registroDeVacinacaoRepository, times(1)).deleteById("ID_VALIDO");
        assertTrue(resultado); // Verifica se o retorno é verdadeiro
    }

    @Test
    public void testApagarRegistroUltimaDoseNaoConfirmada() {
        // Configurando o mock para não confirmar a última dose
        when(registroDeVacinacaoService.confirmacaoUltimaDose("ID_INVALIDO")).thenReturn(false);

        // Criando um mock para registroDeVacinacaoRepository
        RegistroDeVacinacaoRepository registroDeVacinacaoRepository = mock(RegistroDeVacinacaoRepository.class);

        // Chamar o método que queremos testar e capturar a exceção
        ApagarException exception = assertThrows(ApagarException.class, () -> {
            registroDeVacinacaoService.apagarRegistro("ID_INVALIDO");
        });

        // Verificando se a exceção foi lançada corretamente
        assertEquals("Só é possível apagar o último registro de vacinação", exception.getMessage());
        verify(registroDeVacinacaoRepository, never()).deleteById("ID_INVALIDO");
    }
*/
// criarRegistroEditado
    @Test
    public void testCriarRegistroEditado() {
        // Criar um registro de vacinação para editar
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();

        // Mock do ID desejado
        String novoId = "NOVO_ID";

        // Criar um mock da instância da interface do repositório (ou uma instância real, se aplicável)
        RegistroDeVacinacaoRepository registroDeVacinacaoRepository = mock(RegistroDeVacinacaoRepository.class);


        // Chamar o método que queremos testar
        RegistroDeVacinacao registroEditado = registroDeVacinacaoService.criarRegistroEditado(registro, novoId);

        // Verificar se o ID foi alterado
        assertEquals(novoId, registroEditado.getId());

        // Verificar se outros dados permanecem inalterados
        assertEquals(LocalDate.now(), registroEditado.getDataDeVacinacao());
        assertEquals(registro.getIdentificacaoDoPaciente(), registroEditado.getIdentificacaoDoPaciente());
        assertEquals(registro.getIdentificacaoDaVacina(), registroEditado.getIdentificacaoDaVacina());
        assertEquals(registro.getIdentificacaoDaDose(), registroEditado.getIdentificacaoDaDose());
    }
    //validarEdicaoRegistro

    @Test
    public void testValidarEdicaoRegistro_DadosValidos() {
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();

        RegistroDeVacinacao registroAtual = RegistroDeVacinacaoUtils.criarOutroRegistroDeVacinacaoExemplo();

        RegistroDeVacinacaoRepository registroDeVacinacaoRepository = mock(RegistroDeVacinacaoRepository.class);
        // Configuração do mock da API externa 1 (vacina)
        when(interfaceAPI1Service.buscarVacinaDaApi1(registro.getIdentificacaoDaVacina()))
                .thenReturn(ResponseEntity.ok(new Vacina()));

        // Configuração do mock da API externa 2 (paciente)
        when(interfaceAPI2Service.PacienteDaApi2(registro.getIdentificacaoDoPaciente()))
                .thenReturn(ResponseEntity.ok(new Paciente()));

        // Verificação de exceções ou sucesso
        assertDoesNotThrow(() -> registroDeVacinacaoService.validarEdicaoRegistro(registro, registroAtual));
    }

    @Test
    public void testValidarEdicaoRegistro_DoseNaoEditavel() {
        RegistroDeVacinacao registro = RegistroDeVacinacaoUtils.criarRegistroDeVacinacaoExemplo();

        RegistroDeVacinacao registroAtual = RegistroDeVacinacaoUtils.criarOutroRegistroDeVacinacaoExemplo();
        Vacina vacina = VacinaUtils.criarVacinaExemplo();

        // Forçar a identificação da dose ser a mesma
        registro.setIdentificacaoDaDose(2);
        registro.setIdentificacaoDoPaciente(registroAtual.getIdentificacaoDoPaciente());

        // Verificação se a exceção correta é lançada
        assertThrows(EditarException.class, () -> registroDeVacinacaoService.validarEdicaoRegistro(registro, registroAtual));

    }


}