package com.vacinas.ap3.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProfissionalDeSaude {
    private String nome;
    private String cpf;
}
