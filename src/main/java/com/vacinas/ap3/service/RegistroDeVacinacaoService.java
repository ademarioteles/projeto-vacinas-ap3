package com.vacinas.ap3.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.vacinas.ap3.DTO.Endereco;
import com.vacinas.ap3.DTO.Paciente;
import com.vacinas.ap3.DTO.Vacina;
import com.vacinas.ap3.entity.Mensagem;
import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.exceptions.*;
import com.vacinas.ap3.repository.RegistroDeVacinacaoRepository;
import feign.FeignException;
import feign.Response;
import org.aspectj.apache.bcel.generic.ReturnaddressType;
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
            ResponseEntity response = interfaceAPI2Service.PacienteDaApi2(identificacaoDoPaciente);
            return (Paciente) response.getBody();
        } catch (FeignException e) {
            throw new ExteriorException("Paciente não encontrado");
        }
    }

    private Vacina validarVacinaExistente(String identificacaoDaVacina) {
        try {
            ResponseEntity response = interfaceAPI1Service.buscarVacinaDaApi1(identificacaoDaVacina);
            return (Vacina) response.getBody();
        } catch (FeignException e) {
            throw new ExteriorException("Vacina não encontrado");
        }
    }


    private void validarDose(RegistroDeVacinacao registro, List<RegistroDeVacinacao> registros) {
        if (registros.isEmpty()) {
            // Se a lista de registros estiver vazia, a dose cadastrada deve ser a primeira.
            if (registro.getIdentificacaoDaDose() != 1) {
                throw new OrdemDoseInvalidaException("Nenhum registro de vacinação encontrado, essa dose deverá ser a primeira");
            }else {
                return;
            }
        }

        for (RegistroDeVacinacao registroExistente : registros) {
            if (registroExistente.getIdentificacaoDaDose() == registro.getIdentificacaoDaDose()) {
                throw new RegistroExistenteException("Registro de vacinação já existe.");
            }
        }

        Vacina vacinaAplicada = validarVacinaExistente(registro.getIdentificacaoDaVacina());

        if (registro.getIdentificacaoDaDose() > vacinaAplicada.getNumero_de_doses()) {
            throw new DoseMaiorException("Número de dose maior que o permitido.");
        }

        LocalDate dataUltimaDose = registros.stream()
                .map(RegistroDeVacinacao::getDataDeVacinacao)
                .max(Comparator.naturalOrder())
                .orElse(LocalDate.MIN);

        LocalDate dataRegistroAtual = registro.getDataDeVacinacao();
        long intervaloDias = ChronoUnit.DAYS.between(dataUltimaDose, dataRegistroAtual);

        if (intervaloDias < vacinaAplicada.getIntervalo_doses()) {
            throw new IntervaloInsuficienteException("Intervalo insuficiente entre doses.");
        } else {
            // Verifique se a ordem das doses é crescente
            int doseAnterior = registros.isEmpty() ? 0 : registros.get(registros.size() - 1).getIdentificacaoDaDose();
            System.out.println(doseAnterior);
            int novaDose = registro.getIdentificacaoDaDose();

            if (novaDose != doseAnterior + 1) {
                System.out.println(novaDose);
                throw new OrdemDoseInvalidaException("Ordem de Vacinação Inválida");
            }

        }
        if (!registro.getIdentificacaoDaVacina().equals(registros.get(0).getIdentificacaoDaVacina())) {
            throw new VacinaIncompativelException("Vacina diferente das doses anteriores.");
        }
    }


    public ResponseEntity criarRegistroDeVacinacao(RegistroDeVacinacao registroDeVacinacao) {
        try {
            validarPacienteExistente(registroDeVacinacao.getIdentificacaoDoPaciente());
            validarVacinaExistente(registroDeVacinacao.getIdentificacaoDaVacina());
            List<RegistroDeVacinacao> registros = obterRegistroDeVacinacaoPorIdDoPaciente(registroDeVacinacao.getIdentificacaoDoPaciente());
            validarDose(registroDeVacinacao, registros);

            registroDeVacinacaoRepository.save(registroDeVacinacao);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Mensagem("Registro cadastrado com sucesso!"));
        } catch (PacienteInexistenteException | VacinaInexistenteException | DoseMaiorException |
                 RegistroExistenteException | VacinaIncompativelException | IntervaloInsuficienteException |
                 ExteriorException e) {
            // Lidar com exceções
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Mensagem(e.getMessage()));
        }
    }

    public List<RegistroDeVacinacao> listarTodosOsRegistrosDeVacinacao() {
        return registroDeVacinacaoRepository.findAll();
    }

    public List<RegistroDeVacinacao> obterRegistrosDeVacinacaoPorIdDaVacina(String id) {
        List<RegistroDeVacinacao> listaRegistros = listarTodosOsRegistrosDeVacinacao();
        return listaRegistros.stream()
                .filter(registro -> registro.getIdentificacaoDaVacina().equals(id))
                .collect(Collectors.toList());
    }

    public Object obterRegistroResumidoDeVacinacaoPorIdDoPaciente(String id) {
        ResponseEntity pacienteResponse = interfaceAPI2Service.PacienteDaApi2(id);
        Paciente paciente = (Paciente) pacienteResponse.getBody();
        Endereco endereco = paciente.getEndereco();
        List<RegistroDeVacinacao> listaRegistros = listarTodosOsRegistrosDeVacinacao().stream()
                .filter(registro -> registro.getIdentificacaoDoPaciente().equals(id))
                .collect(Collectors.toList());
        Vacina vacina = validarVacinaExistente(listaRegistros.get(0).getIdentificacaoDaVacina());
        Map<String, Object> dados = new HashMap<>();

        Map<String, Object> jPaciente = new HashMap<>();
        jPaciente.put("nome", paciente.getNome());
        jPaciente.put("idade", 58);
        jPaciente.put("bairro", endereco.getBairro());
        jPaciente.put("municipio", endereco.getMunicipio());
        jPaciente.put("estado", endereco.getEstado());

        Map<String, Object> jVacina = new HashMap<>();
        jVacina.put("fabricante", vacina.getFabricante());
        jVacina.put("vacina", vacina.getNome());
        jVacina.put("total_de_doses", vacina.getNumero_de_doses());
        jVacina.put("intervalo_entre_doses", vacina.getIntervalo_doses());

        List<String> jDoses = new ArrayList<>();
        for (RegistroDeVacinacao registro : listaRegistros) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            jDoses.add(registro.getDataDeVacinacao().format(formatter));
        }

        dados.put("doses", jDoses);
        dados.put("vacina", jVacina);
        dados.put("paciente", jPaciente);

        Gson gson = new Gson();
        String json = gson.toJson(dados);
        return gson.fromJson(json, Object.class);
    }

    public Integer obterNumeroDeVacinacao(String estado) {
        if (estado != null) {
            // Se o parâmetro "estado" estiver presente, retorne o total de vacinas aplicadas para esse estado
            ResponseEntity pacientesResponse = interfaceAPI2Service.listarPacientesDaApi2();
            List<Paciente> pacientes = (List<Paciente>) pacientesResponse.getBody();
            List<RegistroDeVacinacao> listaRegistros = listarTodosOsRegistrosDeVacinacao();
            Map<String, Long> pacienteParaQuantidade = listaRegistros.stream()
                    .collect(Collectors.groupingBy(RegistroDeVacinacao::getIdentificacaoDoPaciente, Collectors.counting()));
            Integer quantVacinacaoEstado = 0;
            for (Paciente paciente : pacientes) {
                if (estado.equals(paciente.getEndereco().getEstado())) {
                    String identificacaoPaciente = paciente.getId();
                    Long quantidadeDeRegistros = pacienteParaQuantidade.get(identificacaoPaciente);

                    if (quantidadeDeRegistros != null) {
                        quantVacinacaoEstado += quantidadeDeRegistros.intValue();
                    }
                }
            }
            return quantVacinacaoEstado;
        } else {
            // Caso contrário, retorne o total de vacinas aplicadas de forma geral
            return listarTodosOsRegistrosDeVacinacao().size();
        }
    }

    public List<RegistroDeVacinacao> obterRegistroDeVacinacaoPorIdDoPaciente(String id) {
        List<RegistroDeVacinacao> listaRegistros = listarTodosOsRegistrosDeVacinacao();

        return listaRegistros.stream()
                .filter(registro -> registro.getIdentificacaoDoPaciente().equals(id))
                .collect(Collectors.toList());
    }
}