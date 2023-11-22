package com.vacinas.ap3.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class ProfissionalDeSaude {

    @NotEmpty(message = "O nome da profissional da saude não pode estar vazio.")
    private String nome;

    @NotEmpty(message = "O CPF da profissional da saude não pode estar vazio.")
    private String cpf;
}
