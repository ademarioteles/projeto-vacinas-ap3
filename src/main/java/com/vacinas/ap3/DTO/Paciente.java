package com.vacinas.ap3.DTO;

import lombok.Data;

@Data
public class Paciente {
    private String id;
    private String nome;
    private String sobrenome;
    private String cpf;
    private String dataNascimento;
    private String sexo;
    private String contato;
    private Endereco endereco;
}
