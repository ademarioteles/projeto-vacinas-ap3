package com.vacinas.ap3.service;

import com.vacinas.ap3.DTO.Paciente;
import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.repository.RegistroDeVacinacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistroDeVacinacaoService {
    private RegistroDeVacinacaoRepository registroDeVacinacaoRepository;
    private InterfaceAPI2Service interfaceAPI2Service;

    public RegistroDeVacinacaoService(RegistroDeVacinacaoRepository registroDeVacinacaoRepository, InterfaceAPI2Service interfaceAPI2Service) {
        this.registroDeVacinacaoRepository = registroDeVacinacaoRepository;
        this.interfaceAPI2Service = interfaceAPI2Service;
    }

    public RegistroDeVacinacao criarRegistroDeVacinacao(RegistroDeVacinacao registroDeVacinacao) {
        // Lógica de validação, se necessário
        return registroDeVacinacaoRepository.save(registroDeVacinacao);
    }

    public List<RegistroDeVacinacao> listarTodosOsRegistrosDeVacinacao() {
        return registroDeVacinacaoRepository.findAll();
    }
    public RegistroDeVacinacao obterRegistroDeVacinacaoPorId(String id) {
        return registroDeVacinacaoRepository.findById(id).orElse(null);
    }

    public Integer obterNumeroDeVacinacao(String estado) {
        if (estado != null) {
            // Se o parâmetro "estado" estiver presente, retorne o total de vacinas aplicadas para esse estado
            List<Paciente> pacientes = interfaceAPI2Service.listarPacientesDaApi2();
            List<RegistroDeVacinacao> listaRegistros = listarTodosOsRegistrosDeVacinacao();
            Integer quantVacinacaoEstado = 0;
            for (Paciente paciente : pacientes) {
                if (paciente.getEndereco().getEstado().equals(estado)) {
                    long quantregistro = listaRegistros.stream().filter(objeto -> objeto.getIdentificacaoDoPaciente().equals(paciente.getId())).count();
                    quantVacinacaoEstado += (int) quantregistro;
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