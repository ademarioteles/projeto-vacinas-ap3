package com.vacinas.ap3.service;

import com.vacinas.ap3.DTO.Paciente;
import com.vacinas.ap3.DTO.Vacina;
import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.repository.RegistroDeVacinacaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
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
            List<Vacina> vacinas = interfaceAPI1Service.listarVacinasDaApi1();
            boolean vacinaExiste = vacinas.stream()
                    .anyMatch(vacina -> vacina.getId().equals(registroDeVacinacao.getIdentificacaoDaVacina()));
            if (vacinaExiste) {
                Vacina vacinaAplicada = null;
                for (Vacina vacina : vacinas) {
                    if (vacina.getId().equals(registroDeVacinacao.getIdentificacaoDaVacina())) {
                        // Se encontrarmos a vacina, retorne a quantidade de doses
                        vacinaAplicada = vacina;
                    }
                }
                if (registroDeVacinacao.getIdentificacaoDaDose() > vacinaAplicada.getNumero_de_doses()) {
                    //jogar erro
                    return null;
                } else {
                    if (registroDeVacinacao.getIdentificacaoDaDose() > 1 && registroDeVacinacao.getIdentificacaoDaDose() > 0) {
                        List<RegistroDeVacinacao> registros = obterRegistroDeVacinacaoPorIdDoPaciente(registroDeVacinacao.getIdentificacaoDoPaciente());
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
                            return null;
                        }

                    } else {
                        return registroDeVacinacaoRepository.save(registroDeVacinacao);
                    }
                }

            } else {
                // O vacina não existe, jogar um erro
                return null;
            }
        } else {
            // O paciente não existe, jogar um erro
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

    public List<RegistroDeVacinacao> obterRegistroDeVacinacaoPorIdDoPaciente(String id) {
        List<RegistroDeVacinacao> listaRegistros = listarTodosOsRegistrosDeVacinacao();

        return listaRegistros.stream()
                .filter(registro -> registro.getIdentificacaoDoPaciente().equals(id))
                .collect(Collectors.toList());
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

    // Requisições externas


}