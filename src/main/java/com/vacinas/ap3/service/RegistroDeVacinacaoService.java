package com.vacinas.ap3.service;

import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.repository.RegistroDeVacinacaoRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistroDeVacinacaoService {

    private RegistroDeVacinacaoRepository registroDeVacinacaoRepository;

    public RegistroDeVacinacaoService(RegistroDeVacinacaoRepository registroDeVacinacaoRepository) {
        this.registroDeVacinacaoRepository = registroDeVacinacaoRepository;
    }

    public RegistroDeVacinacao criarRegistroDeVacinacao(RegistroDeVacinacao registroDeVacinacao) {
        // Lógica de validação, se necessário
        return registroDeVacinacaoRepository.save(registroDeVacinacao);
    }

    public RegistroDeVacinacao obterRegistroDeVacinacaoPorId(String id) {
        return registroDeVacinacaoRepository.findById(id).orElse(null);
    }

    // Implemente outros métodos de serviço, se necessário
}