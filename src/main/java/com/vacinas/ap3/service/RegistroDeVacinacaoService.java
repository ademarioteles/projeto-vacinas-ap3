package com.vacinas.ap3.service;

import com.vacinas.ap3.DTO.Endereco;
import com.vacinas.ap3.DTO.Paciente;
import com.vacinas.ap3.DTO.Vacina;
import com.vacinas.ap3.entity.ProfissionalDeSaude;
import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.entity.RegistroDeVacinacaoDoses;
import com.vacinas.ap3.entity.RegistroDeVacinacaoResumido;
import com.vacinas.ap3.enums.Estados;
import com.vacinas.ap3.exceptions.*;
import com.vacinas.ap3.repository.RegistroDeVacinacaoRepository;
import com.vacinas.ap3.util.RegistroDeVacinacaoUtils;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class RegistroDeVacinacaoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericHandlerException.class);
    private RegistroDeVacinacaoRepository registroDeVacinacaoRepository;
    public ClientPacientesService clientPacientesService;
    public ClientVacinasService clientVacinasService;

    public RegistroDeVacinacaoService(RegistroDeVacinacaoRepository registroDeVacinacaoRepository, ClientPacientesService clientPacientesService, ClientVacinasService clientVacinasService) {
        this.registroDeVacinacaoRepository = registroDeVacinacaoRepository;
        this.clientPacientesService = clientPacientesService;
        this.clientVacinasService = clientVacinasService;
    }

    //Valida a existência de um paciente com base na identificação fornecida, consultando uma API externa
    public Paciente validarPacienteExistente(String identificacaoDoPaciente) {
        try {
            ResponseEntity<Paciente> response = clientPacientesService.PacienteDaApi2(identificacaoDoPaciente);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody(); // Paciente encontrado na API externa
            } else {
                throw new ExteriorException("Paciente não encontrado na API externa");
            }
        } catch (FeignException e) {
            throw new ExteriorException("Erro ao buscar paciente na API externa");
        }
    }

    //Valida a existência de uma vacina com base na identificação fornecida, consultando uma API externa.
    public Vacina validarVacinaExistente(String identificacaoDaVacina) {
        try {
            ResponseEntity<Vacina> response = clientVacinasService.buscarVacina(identificacaoDaVacina);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody(); // Paciente encontrado na API externa
            } else {
                throw new ExteriorException("Vacina não encontrado na API externa");
            }
        } catch (FeignException e) {
            throw new ExteriorException("Erro ao buscar Vacina na API externa");
        }
    }

    //Organiza a chamada de diversos metodos, com o intuito de validar se a dose do registro é valida
    private void validarDose(RegistroDeVacinacao registro, List<RegistroDeVacinacao> registros) {
        if (registros.isEmpty()) {
            validarPrimeiraDose(registro);
            return;
        }

        validarDoseExistente(registro, registros);
        Vacina vacinaAplicada = validarVacinaExistente(registro.getIdentificacaoDaVacina());
        Paciente paciente = validarPacienteExistente(registro.getIdentificacaoDoPaciente());
        LocalDate dataUltimaDose = obterDataUltimaDose(registros);
        LocalDate dataRegistroAtual = registro.getDataDeVacinacao();
        validarNumeroDoses(vacinaAplicada, registro.getIdentificacaoDaDose());
        validarIntervaloDoses(dataUltimaDose, dataRegistroAtual, vacinaAplicada, paciente);
        validarOrdemDose(registro, registros);
        validarVacinaIncompativel(registro, registros);
    }

    //joga um excessão caso a dose cadastrada não for a primeira
    public void validarPrimeiraDose(RegistroDeVacinacao registro) {
        if (registro.getIdentificacaoDaDose() != 1) {
            throw new OrdemDoseInvalidaException("Nenhum registro de vacinação encontrado, essa dose deverá ser a primeira");
        }
    }

    //verifica se ja existe uma dose identica cadastrada no banco de dados
    public void validarDoseExistente(RegistroDeVacinacao registro, List<RegistroDeVacinacao> registros) {
        for (RegistroDeVacinacao registroExistente : registros) {
            if (registroExistente.getIdentificacaoDaDose() == registro.getIdentificacaoDaDose()) {
                throw new RegistroExistenteException("Registro de vacinação já existe.");
            }
        }
    }

    //verifica se o intervalo entre doses doses está correto, comparando a data da ultima vacinação com o ultimo registro
    public void validarIntervaloDoses(LocalDate dataUltimaDose, LocalDate dataRegistroAtual, Vacina vacinaAplicada, Paciente paciente) {
        long intervaloDias = ChronoUnit.DAYS.between(dataUltimaDose, dataRegistroAtual);
        LocalDate dataAlvo = ChronoUnit.DAYS.addTo(dataUltimaDose, vacinaAplicada.getIntervalo_doses());
        // Define a custom date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // Format the date using the defined format
        String DataUltimaDoseFormat = dataUltimaDose.format(formatter);
        String dataAlvoFormat = dataAlvo.format(formatter);

        if (intervaloDias < vacinaAplicada.getIntervalo_doses()) {
            throw new IntervaloInsuficienteException("O paciente " + paciente.getNome() + " recebeu uma dose de" + vacinaAplicada.getNome() + " no dia " + DataUltimaDoseFormat +
                    ". A próxima dose deverá ser aplicada a partir do dia " + dataAlvoFormat);
        }
    }

    //verifica se o numero da dose que está sendo cadastrada não é maior que o permitido pela a vacina
    public void validarNumeroDoses(Vacina vacina, int identificacaoDaDose) {
        int numeroDosesRegistradas = vacina.getNumero_de_doses();

        if (identificacaoDaDose > numeroDosesRegistradas) {
            throw new MaximoDoseException("Número de doses inválido para a vacina " + vacina.getNome() +
                    ". O número máximo de doses é " + numeroDosesRegistradas);
        }
    }

    //garante que as doses só possam ser aplicadas em ordem crescente
    public void validarOrdemDose(RegistroDeVacinacao registro, List<RegistroDeVacinacao> registros) {
        int doseAnterior = registros.isEmpty() ? 0 : registros.get(registros.size() - 1).getIdentificacaoDaDose();
        int novaDose = registro.getIdentificacaoDaDose();

        if (novaDose != doseAnterior + 1) {
            throw new OrdemDoseInvalidaException("Ordem de Vacinação Inválida");
        }
    }

    //verifica se a vacina do registro atual é compativel com o historico de registros
    public void validarVacinaIncompativel(RegistroDeVacinacao registro, List<RegistroDeVacinacao> registros) {
        if (!registro.getIdentificacaoDaVacina().equals(registros.get(0).getIdentificacaoDaVacina())) {
            throw new VacinaIncompativelException("Vacina diferente das doses anteriores.");
        }
    }

    //obtem a data da ultima dose com base dos registros anteriores
    public LocalDate obterDataUltimaDose(List<RegistroDeVacinacao> registros) {
        return registros.stream()
                .map(RegistroDeVacinacao::getDataDeVacinacao)
                .max(Comparator.naturalOrder())
                .orElse(LocalDate.MIN);
    }

    //obtem o ultimo registro de vacinação com base numa lista
    public RegistroDeVacinacao obterUltimaDose(List<RegistroDeVacinacao> registros) {
        return registros.stream()
                .max(Comparator.comparingInt(RegistroDeVacinacao::getIdentificacaoDaDose))
                .orElse(null);
    }

    //confirma se a dose com o respectivo id foi a ultima dose aplicada
    public boolean confirmacaoUltimaDose(String id) {
        RegistroDeVacinacao registro = obterRegistroDeVacinacaoPorId(id);
        List<RegistroDeVacinacao> registros = obterRegistroDeVacinacaoPorIdDoPaciente(registro.getIdentificacaoDoPaciente());
        RegistroDeVacinacao ultimoRegistro = obterUltimaDose(registros);

        return ultimoRegistro.getIdentificacaoDaDose() == registro.getIdentificacaoDaDose();
    }

    //garante que não seja cadastrado um registro com data no futuro
    public void validarDataDeVacinacao(LocalDate data) {
        if (data.isAfter(LocalDate.now())) {
            throw new DataInvalidaException("Não é possivel ter um registro com uma data no futuro.");
        }
    }

    //administra as chamadas dos metodos que irão permitir ou não que o registro seja concluido
    public RegistroDeVacinacao criarRegistroDeVacinacao(RegistroDeVacinacao registroDeVacinacao) {
        validarDataDeVacinacao(registroDeVacinacao.getDataDeVacinacao());
        validarPacienteExistente(registroDeVacinacao.getIdentificacaoDoPaciente());
        validarVacinaExistente(registroDeVacinacao.getIdentificacaoDaVacina());
        List<RegistroDeVacinacao> registros = obterRegistroDeVacinacaoPorIdDoPaciente(registroDeVacinacao.getIdentificacaoDoPaciente());
        validarDose(registroDeVacinacao, registros);
        registroDeVacinacaoRepository.insert(registroDeVacinacao);
        LOGGER.info("Registro de vacinação criado" + registroDeVacinacao);
        return registroDeVacinacao;
    }

    //busca no banco de dados todos os registros de vacinação
    public List<RegistroDeVacinacao> listarTodosOsRegistrosDeVacinacao() {
        List<RegistroDeVacinacao> lista = registroDeVacinacaoRepository.findAll();
        if (!lista.isEmpty()) {
            return lista;
        }
        throw new DataBaseException("Não há registros de vacinação válidos (listarTodosOsRegistrosDeVacinacao)");
    }

    //busca no banco de dados todos os registros de vacinação com uma vacina especifica
    public List<RegistroDeVacinacao> obterRegistrosDeVacinacaoPorIdDaVacina(String id) {
        return registroDeVacinacaoRepository.findByIdentificacaoDaVacina(id);
    }

    //busca no banco de dados o registro de vacinação com um id especifico
    public RegistroDeVacinacao obterRegistroDeVacinacaoPorId(String id) {
        Optional<RegistroDeVacinacao> registro = registroDeVacinacaoRepository.findById(id);
        if (!registro.isEmpty()) {
            return registro.get();
        } else {
            throw new RegistroInexistenteException("Registro não encontrado");
        }
    }

    //coleta os cados de vacinação e cria um objeto com os dados coletados
    public RegistroDeVacinacaoResumido obterRegistroResumidoDeVacinacaoPorIdDoPaciente(String id) {
        Paciente paciente = validarPacienteExistente(id);
        Endereco endereco = paciente.getEndereco();
        List<RegistroDeVacinacao> listaRegistros = obterRegistroDeVacinacaoPorIdDoPaciente(id);
        Vacina vacina = validarVacinaExistente(listaRegistros.get(0).getIdentificacaoDaVacina());
        RegistroDeVacinacaoResumido registroResumido = new RegistroDeVacinacaoResumido();


        registroResumido.setNome(paciente.getNome());
        registroResumido.setIdade(calculoIdade(paciente.getDataNascimento()));
        registroResumido.setBairro(endereco.getBairro());
        registroResumido.setMunicipio(endereco.getMunicipio());
        registroResumido.setEstado(endereco.getEstado());
        registroResumido.setFabricanteVacina(vacina.getFabricante());
        registroResumido.setNomeVacina(vacina.getNome());
        registroResumido.setTotalDeDosesVacina(vacina.getNumero_de_doses());
        registroResumido.setIntervaloEntreDoses(vacina.getIntervalo_doses());

        List<String> Doses = new ArrayList<>();
        for (RegistroDeVacinacao registro : listaRegistros) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            Doses.add(registro.getDataDeVacinacao().format(formatter));
        }
        registroResumido.setDoses(Doses);

        return registroResumido;
    }

    //recebe a data de nascimento e calcula a idade
    public Integer calculoIdade(String dataDeNascimento) {
        // Formato da data (ano-mês-dia)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Converter a string em um LocalDate
        LocalDate dataNascimento = LocalDate.parse(dataDeNascimento, formatter);
        // Calcular a idade
        LocalDate dataAtual = LocalDate.now();
        int idade = Period.between(dataNascimento, dataAtual).getYears();

        return idade;
    }

    //retorna o total de vacinações gerais ou por estado
    public Map<String, Object> obterNumeroDeVacinacao(String estado) {
        Map<String, Object> resposta = new HashMap<>();
        validarEstado(estado);
        int resultado = 0;
        if (estado != null) {
            List<RegistroDeVacinacao> listaRegistros = listarTodosOsRegistrosDeVacinacao();
            long contagem = listaRegistros.stream()
                    .filter(registro -> estado.toUpperCase().equals(obterEstadoDoPaciente(registro.getIdentificacaoDoPaciente())))
                    .count();
            resultado = (int) contagem;
        }else {
            resultado = listarTodosOsRegistrosDeVacinacao().size();
        }
        resposta.put("O total de vacinas aplicadas é de", resultado);
            return  resposta;
    }

    //usa um metodo para buscar na api2 o paciente e busca o estado.
    public String obterEstadoDoPaciente(String id) {
        Paciente paciente = validarPacienteExistente(id);
        return paciente.getEndereco().getEstado().toUpperCase();
    }

    //busca no banco de dados todos os registros de vacinação de um paciente especifico
    public List<RegistroDeVacinacao> obterRegistroDeVacinacaoPorIdDoPaciente(String id) {
        return registroDeVacinacaoRepository.findByIdentificacaoDoPaciente(id);
    }

    //coloca em uma lista todos os pacientes atrasados
    public List<Paciente> obterPacientesAtrasados(String estado) {
        LocalDate dataAtual = LocalDate.now();
        List<RegistroDeVacinacao> registros = listarTodosOsRegistrosDeVacinacao();
        Set<Paciente> pacientesAtrasadosSet = new HashSet<>();

        for (RegistroDeVacinacao registro : registros) {
            List<RegistroDeVacinacao> registrosPorPaciente = obterRegistroDeVacinacaoPorIdDoPaciente(registro.getIdentificacaoDoPaciente());

            RegistroDeVacinacao ultimaDose = registrosPorPaciente.stream()
                    .max(Comparator.comparing(RegistroDeVacinacao::getDataDeVacinacao))
                    .orElse(null);

            if (ultimaDose != null) {
                Vacina ultimaVacina = validarVacinaExistente(ultimaDose.getIdentificacaoDaVacina());

                // Verificar se o número de doses da última vacina é diferente da identificação da última dose
                if (ultimaVacina.getNumero_de_doses() != ultimaDose.getIdentificacaoDaDose()) {
                    LocalDate dataAlvo = ultimaDose.getDataDeVacinacao().plusDays(ultimaVacina.getIntervalo_doses());

                    if (dataAlvo.isBefore(dataAtual)) {
                        pacientesAtrasadosSet.add(validarPacienteExistente(registro.getIdentificacaoDoPaciente()));
                    }
                }
            }
        }

        List<Paciente> pacientesAtrasados = new ArrayList<>(pacientesAtrasadosSet);

        if (estado == null) {
            return pacientesAtrasados;
        } else {
            // Filtra pacientes pelo estado, se necessário
            return pacientesAtrasados.stream()
                    .filter(paciente -> paciente.getEndereco().getEstado().toUpperCase().equals(estado.toUpperCase()))
                    .collect(Collectors.toList());
        }
    }

    /*retorna uma lista com registros de vacinação dose com base,
    resumo com fabricante, vacinas e doses aplicadas, filtradas por estado e/ou fabricante*/
    public List<RegistroDeVacinacaoDoses> obterDosesAplicadas(String estado, String fabricantes) {
        if (estado != null){
            validarEstado(estado);
        }
        List<Vacina> vacinasUnicas = new ArrayList<>();
        List<RegistroDeVacinacaoDoses> registroDeVacinacaoDoses = new ArrayList<>();
        List<RegistroDeVacinacao> registros = listarTodosOsRegistrosDeVacinacao();
        for (RegistroDeVacinacao registro : registros) {
            String estadoPaciente = obterEstadoDoPaciente(registro.getIdentificacaoDoPaciente());
            Vacina vacina = validarVacinaExistente(registro.getIdentificacaoDaVacina());
            if (fabricantes == null && estado == null) {
                if (!vacinasUnicas.contains(vacina)) {
                    vacinasUnicas.add(vacina);
                }
            } else if (fabricantes != null && estado == null) {
                if (vacina.getFabricante().toUpperCase().equals(fabricantes.toUpperCase())) {
                    if (!vacinasUnicas.contains(vacina)) {
                        vacinasUnicas.add(vacina);
                    }
                }
            } else {
                if (vacina.getFabricante().equals(fabricantes) && estadoPaciente.equals(estado.toUpperCase())) {
                    if (!vacinasUnicas.contains(vacina)) {
                        vacinasUnicas.add(vacina);
                    }
                }
            }
        }
        for (Vacina vacina : vacinasUnicas) {
            RegistroDeVacinacaoDoses registroDoses = new RegistroDeVacinacaoDoses();
            Integer quantidadeDoses = 0;
            registroDoses.setFabricante(vacina.getFabricante());
            registroDoses.setVacina(vacina.getNome());
            for (RegistroDeVacinacao registro : registros) {
                if (registro.getIdentificacaoDaVacina().equals(vacina.getId())) {
                    quantidadeDoses++;
                }
            }
            registroDoses.setDosesAplicadas(quantidadeDoses);
            registroDeVacinacaoDoses.add(registroDoses);
        }
        if (registroDeVacinacaoDoses.isEmpty()) {
            throw new RegistroInexistenteException("Nenhum registro encontrado.");
        } else {
            return registroDeVacinacaoDoses;
        }
    }

    //apaga um registro de vacinação
    public Boolean apagarRegistro(String id) {
        if (confirmacaoUltimaDose(id)) {
            LOGGER.info("Registro de vacinação apagado. ID: " + id);
            registroDeVacinacaoRepository.deleteById(id);
            return true;
        } else {
            throw new ApagarException("Só é possivel apagar o ultimo registro de vacinação");
        }
    }

    //Valida se o estado é valido
    public Boolean validarEstado(String estado) {
        estado = estado.toUpperCase(); // Converte para maiúsculas
        for (Estados e : Estados.values()) {
            if (e.name().equals(estado)) {
                return true; // Encontrou um estado válido
            }
        }
        throw new EstadoInvalidoException("Estado Invalido");
    }

    // edita um registro de vacinação existente
    public RegistroDeVacinacao editarRegistroDeVacinacao(RegistroDeVacinacao registroDeVacinacao, String id) {
        if (confirmacaoUltimaDose(id)) {
            RegistroDeVacinacao registroAtual = obterRegistroDeVacinacaoPorId(id);
            validarEdicaoRegistro(registroDeVacinacao, registroAtual);
            RegistroDeVacinacao registroEditado = criarRegistroEditado(registroDeVacinacao, id);
            salvarRegistroEditado(registroEditado);
            return registroEditado;
        } else {
            throw new EditarException("Só é possível editar o último registro de vacinação");
        }
    }

    //edita informações de um registro de informações parcialmente, com base em palavra chaves
    public RegistroDeVacinacao editarRegistroDeVacinacaoParcial(String id, Map<String, Object> atualizacao) {
        RegistroDeVacinacao registroAtualizado = obterRegistroDeVacinacaoPorId(id);
        ProfissionalDeSaude profissionalDeSaude = registroAtualizado.getProfissionalDeSaude();
        List<String> itens = new ArrayList<>();

        for (Map.Entry<String, Object> entry : atualizacao.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            switch (key) {
                case "dataDeVacinacao":
                    itens.add("dataDeVacinacao");
                    registroAtualizado.setDataDeVacinacao((LocalDate) value);
                    break;
                case "identificacaoDoPaciente":
                    itens.add("identificacaoDoPaciente");
                    registroAtualizado.setIdentificacaoDoPaciente((String) value);
                    break;
                case "identificacaoDaVacina":
                    itens.add("identificacaoDaVacina");
                    registroAtualizado.setIdentificacaoDaVacina((String) value);
                    break;
                case "identificacaoDaDose":
                    itens.add("identificacaoDaDose");
                    registroAtualizado.setIdentificacaoDaDose((Integer) value);
                    break;
                case "nome":
                    itens.add("nome");
                    profissionalDeSaude.setNome((String) value);
                    break;
                case "cpf":
                    itens.add("cpf");
                    profissionalDeSaude.setCpf((String) value);
                    break;
                default:
                    throw new ChaveInvalidaException("Chave de atualização inválida: " + key);
            }
        }
        for (String elemento : itens) {
            if (!elemento.matches("nome") && !elemento.matches("cpf")) {
                editarRegistroDeVacinacao(registroAtualizado, id);
            }
        }
        salvarRegistroEditado(registroAtualizado);
        return registroAtualizado;
    }

    //verifica se os dados a serem editados são validos
    public void validarEdicaoRegistro(RegistroDeVacinacao registroDeVacinacao, RegistroDeVacinacao registroAtual) {
        if (registroAtual.getIdentificacaoDoPaciente().equals(registroDeVacinacao.getIdentificacaoDoPaciente()) &&
                registroAtual.getIdentificacaoDaDose() != registroDeVacinacao.getIdentificacaoDaDose()) {
            throw new EditarException("Não é possível editar a dose desse registro de vacinação");
        }
        validarPacienteExistente(registroDeVacinacao.getIdentificacaoDoPaciente());
        validarVacinaExistente(registroDeVacinacao.getIdentificacaoDaVacina());
        List<RegistroDeVacinacao> registros = obterRegistroDeVacinacaoPorIdDoPaciente(registroDeVacinacao.getIdentificacaoDoPaciente());
    }

    //altera o id do objeto gerado para o id do objeto a ser editado
    public RegistroDeVacinacao criarRegistroEditado(RegistroDeVacinacao registroDeVacinacao, String id) {
        RegistroDeVacinacao registroEditado = registroDeVacinacao;
        registroEditado.setId(id);
        return registroEditado;
    }

    //salva registro no banco de dados
    public void salvarRegistroEditado(RegistroDeVacinacao registroEditado) {
        LOGGER.info("Registro de vacinação editado. " + registroEditado);
        registroDeVacinacaoRepository.save(registroEditado);
    }

    //injeta dados no banco para testes
    public void injetarDados() {
        List<RegistroDeVacinacao> registrosInject = RegistroDeVacinacaoUtils.injectDados();
        for (RegistroDeVacinacao registro : registrosInject) {
            registroDeVacinacaoRepository.save(registro);
        }
    }


}