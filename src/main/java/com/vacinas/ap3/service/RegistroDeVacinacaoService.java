package com.vacinas.ap3.service;

import com.vacinas.ap3.DTO.Endereco;
import com.vacinas.ap3.DTO.Paciente;
import com.vacinas.ap3.DTO.Vacina;
import com.vacinas.ap3.entity.ProfissionalDeSaude;
import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.entity.RegistroDeVacinacaoDoses;
import com.vacinas.ap3.entity.RegistroDeVacinacaoResumido;
import com.vacinas.ap3.exceptions.*;
import com.vacinas.ap3.repository.RegistroDeVacinacaoRepository;
import feign.FeignException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
public class RegistroDeVacinacaoService {
    private RegistroDeVacinacaoRepository registroDeVacinacaoRepository;
    private InterfaceAPI2Service interfaceAPI2Service;
    private InterfaceAPI1Service interfaceAPI1Service;

    public RegistroDeVacinacaoService(RegistroDeVacinacaoRepository registroDeVacinacaoRepository, InterfaceAPI2Service interfaceAPI2Service, InterfaceAPI1Service interfaceAPI1Service) {
        this.registroDeVacinacaoRepository = registroDeVacinacaoRepository;
        this.interfaceAPI2Service = interfaceAPI2Service;
        this.interfaceAPI1Service = interfaceAPI1Service;
    }

    public Paciente validarPacienteExistente(String identificacaoDoPaciente) {
        try {
            ResponseEntity<Paciente> response = interfaceAPI2Service.PacienteDaApi2(identificacaoDoPaciente);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody(); // Paciente encontrado na API externa
            } else {
                throw new ExteriorException("Paciente não encontrado na API externa");
            }
        } catch (FeignException e) {
            throw new ExteriorException("Erro ao buscar paciente na API externa");
        }
    }

    public Vacina validarVacinaExistente(String identificacaoDaVacina) {
        try {
            ResponseEntity<Vacina> response = interfaceAPI1Service.buscarVacinaDaApi1(identificacaoDaVacina);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody(); // Paciente encontrado na API externa
            } else {
                throw new ExteriorException("Vacina não encontrado na API externa");
            }
        } catch (FeignException e) {
            throw new ExteriorException("Erro ao buscar Vacina na API externa");
        }
    }

    private void validarDose(RegistroDeVacinacao registro, List<RegistroDeVacinacao> registros) {
        if (registros.isEmpty()) {
            validarPrimeiraDose(registro);
            return;
        }

        validarDoseExistente(registro, registros);
        Vacina vacinaAplicada = validarVacinaExistente(registro.getIdentificacaoDaVacina());

        LocalDate dataUltimaDose = obterDataUltimaDose(registros);
        LocalDate dataRegistroAtual = registro.getDataDeVacinacao();
        validarIntervaloDoses(dataUltimaDose, dataRegistroAtual, vacinaAplicada);

        validarOrdemDose(registro, registros);

        validarVacinaIncompativel(registro, registros);
    }

    public void validarPrimeiraDose(RegistroDeVacinacao registro) {
        if (registro.getIdentificacaoDaDose() != 1) {
            throw new OrdemDoseInvalidaException("Nenhum registro de vacinação encontrado, essa dose deverá ser a primeira");
        }
    }

    public void validarDoseExistente(RegistroDeVacinacao registro, List<RegistroDeVacinacao> registros) {
        for (RegistroDeVacinacao registroExistente : registros) {
            if (registroExistente.getIdentificacaoDaDose() == registro.getIdentificacaoDaDose()) {
                throw new RegistroExistenteException("Registro de vacinação já existe.");
            }
        }
    }
    public void validarIntervaloDoses(LocalDate dataUltimaDose, LocalDate dataRegistroAtual, Vacina vacinaAplicada) {
        long intervaloDias = ChronoUnit.DAYS.between(dataUltimaDose, dataRegistroAtual);
        if (intervaloDias < vacinaAplicada.getIntervalo_doses()) {
            throw new IntervaloInsuficienteException("Intervalo insuficiente entre doses.");
        }
    }
    public void validarOrdemDose(RegistroDeVacinacao registro, List<RegistroDeVacinacao> registros) {
        int doseAnterior = registros.isEmpty() ? 0 : registros.get(registros.size() - 1).getIdentificacaoDaDose();
        int novaDose = registro.getIdentificacaoDaDose();

        if (novaDose != doseAnterior + 1) {
            throw new OrdemDoseInvalidaException("Ordem de Vacinação Inválida");
        }
    }
    public void validarVacinaIncompativel(RegistroDeVacinacao registro, List<RegistroDeVacinacao> registros) {
        if (!registro.getIdentificacaoDaVacina().equals(registros.get(0).getIdentificacaoDaVacina())) {
            throw new VacinaIncompativelException("Vacina diferente das doses anteriores.");
        }
    }

    public LocalDate obterDataUltimaDose(List<RegistroDeVacinacao> registros) {
        return registros.stream()
                .map(RegistroDeVacinacao::getDataDeVacinacao)
                .max(Comparator.naturalOrder())
                .orElse(LocalDate.MIN);
    }

    public RegistroDeVacinacao obterUltimaDose(List<RegistroDeVacinacao> registros) {
        return registros.stream()
                .max(Comparator.comparingInt(RegistroDeVacinacao::getIdentificacaoDaDose))
                .orElse(null);
    }

    public boolean confirmacaoUltimaDose(String id) {
        RegistroDeVacinacao registro = obterRegistroDeVacinacaoPorId(id);
        List<RegistroDeVacinacao> registros = obterRegistroDeVacinacaoPorIdDoPaciente(registro.getIdentificacaoDoPaciente());
        RegistroDeVacinacao ultimoRegistro = obterUltimaDose(registros);

        return ultimoRegistro.getIdentificacaoDaDose() == registro.getIdentificacaoDaDose();
    }

    public void validarDataDeVacinacao(LocalDate data) {
        if (data.isAfter(LocalDate.now())) {
            throw new DataInvalidaException("Não é possivel ter um registro com uma data no futuro.");
        }
    }

    public Boolean criarRegistroDeVacinacao(RegistroDeVacinacao registroDeVacinacao) {
        validarDataDeVacinacao(registroDeVacinacao.getDataDeVacinacao());
        validarPacienteExistente(registroDeVacinacao.getIdentificacaoDoPaciente());
        validarVacinaExistente(registroDeVacinacao.getIdentificacaoDaVacina());
        List<RegistroDeVacinacao> registros = obterRegistroDeVacinacaoPorIdDoPaciente(registroDeVacinacao.getIdentificacaoDoPaciente());
        validarDose(registroDeVacinacao, registros);
        registroDeVacinacaoRepository.save(registroDeVacinacao);
        return true;
    }

    public List<RegistroDeVacinacao> listarTodosOsRegistrosDeVacinacao() {
        try {
            List<RegistroDeVacinacao> lista = registroDeVacinacaoRepository.findAll();
            if (!lista.isEmpty()) {
                return lista;
            }
            throw new DataBaseException("Não há registros de vacinação válidos (listarTodosOsRegistrosDeVacinacao)");
        } catch (DataAccessException ex) {
            throw new DataBaseException("Erro ao listar registros de vacinação (listarTodosOsRegistrosDeVacinacao)");
        }
    }

    public List<RegistroDeVacinacao> obterRegistrosDeVacinacaoPorIdDaVacina(String id) {
        return registroDeVacinacaoRepository.findByIdentificacaoDaVacina(id);
    }

    public RegistroDeVacinacao obterRegistroDeVacinacaoPorId(String id) {
        Optional<RegistroDeVacinacao> registro = registroDeVacinacaoRepository.findById(id);
        if (!registro.isEmpty()) {
            return registro.get();
        } else {
            throw new RegistroInexistenteException("Registro não encontrado");
        }
    }

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

    public Integer obterNumeroDeVacinacao(String estado) {
        if (estado != null) {
            List<RegistroDeVacinacao> listaRegistros = listarTodosOsRegistrosDeVacinacao();
            long contagem = listaRegistros.stream()
                    .filter(registro -> estado.equals(obterEstadoDoPaciente(registro.getIdentificacaoDoPaciente())))
                    .count();
            return (int) contagem;
        } else {
            return listarTodosOsRegistrosDeVacinacao().size();
        }
    }

    public String obterEstadoDoPaciente(String id) {
        Paciente paciente = validarPacienteExistente(id);
        return paciente.getEndereco().getEstado();
    }

    public List<RegistroDeVacinacao> obterRegistroDeVacinacaoPorIdDoPaciente(String id) {
        return registroDeVacinacaoRepository.findByIdentificacaoDoPaciente(id);
    }

    public List<Paciente> obterPacientesAtrasados(String estado) {
        LocalDate dataAtual = LocalDate.now();
        List<RegistroDeVacinacao> registros = listarTodosOsRegistrosDeVacinacao();
        List<Paciente> pacientesAtrasados = new ArrayList<>();

        for (RegistroDeVacinacao registro : registros) {
            List<RegistroDeVacinacao> registrosPorPaciente = obterRegistroDeVacinacaoPorIdDoPaciente(registro.getIdentificacaoDoPaciente());

            RegistroDeVacinacao ultimaDose = registrosPorPaciente.stream()
                    .max(Comparator.comparing(RegistroDeVacinacao::getDataDeVacinacao))
                    .orElse(null);

            if (ultimaDose != null) {
                Vacina ultimaVacina = validarVacinaExistente(ultimaDose.getIdentificacaoDaVacina());
                LocalDate dataAlvo = ultimaDose.getDataDeVacinacao().plusDays(ultimaVacina.getIntervalo_doses());

                if (dataAlvo.isBefore(dataAtual)) {
                    pacientesAtrasados.add(validarPacienteExistente(registro.getIdentificacaoDoPaciente()));
                }
            }
        }

        if (estado == null) {
            return pacientesAtrasados;
        } else {
            List<Paciente> pacientesFiltrados = new ArrayList<>();
            for (Paciente paciente : pacientesAtrasados) {
                Endereco endereco = paciente.getEndereco();
                if (endereco.getEstado().equals(estado)) {
                    pacientesFiltrados.add(paciente);
                }
            }
            return pacientesFiltrados;
        }
    }

    public List<RegistroDeVacinacaoDoses> obterDosesAplicadas(String estado, String fabricantes) {
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
                if (vacina.getFabricante().equals(fabricantes)) {
                    if (!vacinasUnicas.contains(vacina)) {
                        vacinasUnicas.add(vacina);
                    }
                }
            } else {
                if (vacina.getFabricante().equals(fabricantes) && estadoPaciente.equals(estado)) {
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
        return registroDeVacinacaoDoses;
    }

    public Boolean apagarRegistro(String id) {
        if (confirmacaoUltimaDose(id)) {
            registroDeVacinacaoRepository.deleteById(id);
            return true;
        } else {
            throw new ApagarException("Só é possivel apagar o ultimo registro de vacinação");
        }
    }

    public Boolean editarRegistroDeVacinacao(RegistroDeVacinacao registroDeVacinacao, String id) {
        if (confirmacaoUltimaDose(id)) {
            RegistroDeVacinacao registroAtual = obterRegistroDeVacinacaoPorId(id);
            validarEdicaoRegistro(registroDeVacinacao, registroAtual);
            RegistroDeVacinacao registroEditado = criarRegistroEditado(registroDeVacinacao, id);
            salvarRegistroEditado(registroEditado);
            return true;
        } else {
            throw new EditarException("Só é possível editar o último registro de vacinação");
        }
    }

    public Boolean editarRegistroDeVacinacaoParcial(String id, Map<String, Object> atualizacao) {
        RegistroDeVacinacao registroAtualizado = obterRegistroDeVacinacaoPorId(id);
        ProfissionalDeSaude profissionalDeSaude = registroAtualizado.getProfissionalDeSaude();
        List <String> itens = new ArrayList<>();

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
            }
        }
        for (String elemento : itens) {
            if (!elemento.matches("nome") && !elemento.matches("cpf")) {
                return editarRegistroDeVacinacao(registroAtualizado, id);
            }
        }
        salvarRegistroEditado(registroAtualizado);
        return true;
    }

    private void validarEdicaoRegistro(RegistroDeVacinacao registroDeVacinacao, RegistroDeVacinacao registroAtual) {
        if (registroAtual.getIdentificacaoDoPaciente().equals(registroDeVacinacao.getIdentificacaoDoPaciente()) &&
                registroAtual.getIdentificacaoDaDose() != registroDeVacinacao.getIdentificacaoDaDose()) {

            throw new EditarException("Não é possível editar a dose desse registro de vacinação");
        }
        validarPacienteExistente(registroDeVacinacao.getIdentificacaoDoPaciente());
        validarVacinaExistente(registroDeVacinacao.getIdentificacaoDaVacina());
        List<RegistroDeVacinacao> registros = obterRegistroDeVacinacaoPorIdDoPaciente(registroDeVacinacao.getIdentificacaoDoPaciente());
        validarDose(registroDeVacinacao, registros);
    }

    private RegistroDeVacinacao criarRegistroEditado(RegistroDeVacinacao registroDeVacinacao, String id) {
        RegistroDeVacinacao registroEditado = registroDeVacinacao;
        registroEditado.setId(id);
        return registroEditado;
    }

    private void salvarRegistroEditado(RegistroDeVacinacao registroEditado) {
        registroDeVacinacaoRepository.save(registroEditado);
    }

}