package com.vacinas.ap3.service;

import com.vacinas.ap3.DTO.Endereco;
import com.vacinas.ap3.DTO.Paciente;
import com.vacinas.ap3.DTO.Vacina;
import com.vacinas.ap3.entity.Mensagem;
import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.entity.RegistroDeVacinacaoResumido;
import com.vacinas.ap3.exceptions.*;
import com.vacinas.ap3.repository.RegistroDeVacinacaoRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


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

    private Paciente validarPacienteExistente(String identificacaoDoPaciente) {
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

    private List<Paciente> validarListaPacientes() {
        try {
            ResponseEntity<List<Paciente>> response = interfaceAPI2Service.listarPacientesDaApi2();
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody(); // Paciente encontrado na API externa
            } else {
                throw new ExteriorException("Paciente não encontrado na API externa");
            }
        } catch (FeignException e) {
            throw new ExteriorException("Erro ao buscar paciente na API externa");
        }
    }

    private Vacina validarVacinaExistente(String identificacaoDaVacina) {
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

    private void validarPrimeiraDose(RegistroDeVacinacao registro) {
        if (registro.getIdentificacaoDaDose() != 1) {
            throw new OrdemDoseInvalidaException("Nenhum registro de vacinação encontrado, essa dose deverá ser a primeira");
        }
    }

    private void validarDoseExistente(RegistroDeVacinacao registro, List<RegistroDeVacinacao> registros) {
        for (RegistroDeVacinacao registroExistente : registros) {
            if (registroExistente.getIdentificacaoDaDose() == registro.getIdentificacaoDaDose()) {
                throw new RegistroExistenteException("Registro de vacinação já existe.");
            }
        }
    }

    private LocalDate obterDataUltimaDose(List<RegistroDeVacinacao> registros) {
        return registros.stream()
                .map(RegistroDeVacinacao::getDataDeVacinacao)
                .max(Comparator.naturalOrder())
                .orElse(LocalDate.MIN);
    }

    private void validarIntervaloDoses(LocalDate dataUltimaDose, LocalDate dataRegistroAtual, Vacina vacinaAplicada) {
        long intervaloDias = ChronoUnit.DAYS.between(dataUltimaDose, dataRegistroAtual);
        if (intervaloDias < vacinaAplicada.getIntervalo_doses()) {
            throw new IntervaloInsuficienteException("Intervalo insuficiente entre doses.");
        }
    }

    private void validarOrdemDose(RegistroDeVacinacao registro, List<RegistroDeVacinacao> registros) {
        int doseAnterior = registros.isEmpty() ? 0 : registros.get(registros.size() - 1).getIdentificacaoDaDose();
        int novaDose = registro.getIdentificacaoDaDose();

        if (novaDose != doseAnterior + 1) {
            throw new OrdemDoseInvalidaException("Ordem de Vacinação Inválida");
        }
    }

    private void validarVacinaIncompativel(RegistroDeVacinacao registro, List<RegistroDeVacinacao> registros) {
        if (!registro.getIdentificacaoDaVacina().equals(registros.get(0).getIdentificacaoDaVacina())) {
            throw new VacinaIncompativelException("Vacina diferente das doses anteriores.");
        }
    }


    public Boolean criarRegistroDeVacinacao(RegistroDeVacinacao registroDeVacinacao) {
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
        try {
            return registroDeVacinacaoRepository.findByIdentificacaoDaVacina(id);
        } catch (DataAccessException ex) {
            throw new DataBaseException("Erro ao listar registros de vacinação (obterRegistrosDeVacinacaoPorIdDaVacina)");
        }
    }

    public  RegistroDeVacinacaoResumido obterRegistroResumidoDeVacinacaoPorIdDoPaciente(String id) {
        Paciente paciente = validarPacienteExistente(id);
        Endereco endereco = paciente.getEndereco();
        List<RegistroDeVacinacao> listaRegistros = obterRegistroDeVacinacaoPorIdDoPaciente(id);
        Vacina vacina = validarVacinaExistente(listaRegistros.get(0).getIdentificacaoDaVacina());
        RegistroDeVacinacaoResumido registroResumido = new RegistroDeVacinacaoResumido();

        registroResumido.setNome(paciente.getNome());
        registroResumido.setIdade(58); // calcular idade
        registroResumido.setBairro(endereco.getBairro());
        registroResumido.setMunicipio(endereco.getMunicipio());
        registroResumido.setEstado(endereco.getEstado());
        registroResumido.setFabricanteVacina(vacina.getFabricante());
        registroResumido.setNomeVacina(vacina.getNome());
        registroResumido.setTotalDeDosesVacina(vacina.getNumero_de_doses());
        registroResumido.setIntervaloEntreDoses(vacina.getIntervalo_doses());

        List<String> Doses = new ArrayList<>();
        for (RegistroDeVacinacao registro : listaRegistros) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Doses.add(registro.getDataDeVacinacao().format(formatter));
        }
        registroResumido.setDoses(Doses);

        return registroResumido;
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

    private String obterEstadoDoPaciente(String id) {
        Paciente paciente = validarPacienteExistente(id);
        return paciente.getEndereco().getEstado();
    }

    public List<RegistroDeVacinacao> obterRegistroDeVacinacaoPorIdDoPaciente(String id) {
            return registroDeVacinacaoRepository.findByIdentificacaoDoPaciente(id);
    }
}