package com.vacinas.ap3.service;

import com.vacinas.ap3.DTO.Endereco;
import com.vacinas.ap3.DTO.Paciente;
import com.vacinas.ap3.DTO.Vacina;
import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.repository.RegistroDeVacinacaoRepository;
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

    public RegistroDeVacinacao criarRegistroDeVacinacao(RegistroDeVacinacao registroDeVacinacao) {
        // Lógica de validação, se necessário
        List<Paciente> pacientes = interfaceAPI2Service.listarPacientesDaApi2();
        boolean pacienteExiste = pacientes.stream()
                .anyMatch(paciente -> paciente.getId().equals(registroDeVacinacao.getIdentificacaoDoPaciente()));

        if (pacienteExiste) {
            System.out.println("Passou paciente existe");
            List<RegistroDeVacinacao> registros = obterRegistroDeVacinacaoPorIdDoPaciente(registroDeVacinacao.getIdentificacaoDoPaciente());
            List<Vacina> vacinas = interfaceAPI1Service.listarVacinasDaApi1();
            boolean vacinaExiste = vacinas.stream()
                    .anyMatch(vacina -> vacina.getId().equals(registroDeVacinacao.getIdentificacaoDaVacina()));

            if (vacinaExiste) {
                System.out.println("Passou maior vacina existe");
                Vacina vacinaAplicada = null;
                for (Vacina vacina : vacinas) {
                    if (vacina.getId().equals(registroDeVacinacao.getIdentificacaoDaVacina())) {
                        vacinaAplicada = vacina;
                    }
                }
                if (registroDeVacinacao.getIdentificacaoDaDose() > vacinaAplicada.getNumero_de_doses()) {
                    // Jogar erro se a dose for maior que o número de doses da vacina
                    System.out.println("Número de dose maior que o permitido");
                    return null;
                } else {
                    if (!registros.stream().anyMatch(registro -> registro.getIdentificacaoDaDose() == registroDeVacinacao.getIdentificacaoDaDose()) || registros.isEmpty()) {
                        System.out.println("Passou está vazio ou a dose não é igual");
                        System.out.println(registros);
                        if (registroDeVacinacao.getIdentificacaoDaDose() > 1 && registroDeVacinacao.getIdentificacaoDaDose() > 0) {
                            System.out.println("Passou maior que uma");
                            if (registros.stream()
                                    .anyMatch(registro -> registro.getIdentificacaoDaVacina().equals(registroDeVacinacao.getIdentificacaoDaVacina()))) {
                                System.out.println("Passou vacina igual");
                                LocalDate dataUltimaDose = LocalDate.MIN; // Inicialize com a menor data possível
                                LocalDate dataRegistroAtual = registroDeVacinacao.getDataDeVacinacao();
                                for (RegistroDeVacinacao registro : registros) {
                                    LocalDate dataAplicacao = registro.getDataDeVacinacao();
                                    if (dataAplicacao.isAfter(dataUltimaDose)) {
                                        dataUltimaDose = dataAplicacao;
                                    }
                                }
                                // Calcula o intervalo em dias entre a data do registro atual e a data da última dose
                                long intervaloDias = ChronoUnit.DAYS.between(dataUltimaDose, dataRegistroAtual);
                                if (intervaloDias >= vacinaAplicada.getIntervalo_doses()) {
                                    return registroDeVacinacaoRepository.save(registroDeVacinacao);
                                } else {
                                    // Jogar erro se o intervalo não for suficiente
                                    System.out.println("Intervalo insuficiente entre doses");
                                    return null;
                                }
                            } else {
                                // Jogar erro se a vacina não for a mesma que a do paciente
                                System.out.println("Vacina diferente das doses anteriores");
                                return null;
                            }
                        } else {
                            // Jogar erro se a dose for menor ou igual a zero
                            System.out.println("Número de dose inválido");
                            return registroDeVacinacaoRepository.save(registroDeVacinacao);
                        }
                    } else {
                        // Jogar erro se o registro de vacinação já existe
                        System.out.println("Registro de vacinação já existe");
                        return null;
                    }
                }
            } else {
                // Jogar erro se a vacina não existe
                System.out.println("A vacina não existe");
                return null;
            }
        } else {
            // Jogar erro se o paciente não existe
            System.out.println("O paciente não existe");
            return null;
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
        Paciente paciente = interfaceAPI2Service.PacienteDaApi2(id);
        Endereco endereco = paciente.getEndereco();
        List<RegistroDeVacinacao> listaRegistros = listarTodosOsRegistrosDeVacinacao().stream()
                .filter(registro -> registro.getIdentificacaoDoPaciente().equals(id))
                .collect(Collectors.toList());
        List<Vacina> vacinas = interfaceAPI1Service.listarVacinasDaApi1();
        Optional<Vacina> vacinaOptional = vacinas.stream()
                .filter(vacinaFilter -> vacinaFilter.getId().equals(listaRegistros.get(0).getIdentificacaoDaVacina()))
                .findFirst();
        Vacina vacina = vacinaOptional.get();

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
        for (RegistroDeVacinacao registro : listaRegistros){
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
            List<Paciente> pacientes = interfaceAPI2Service.listarPacientesDaApi2();
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