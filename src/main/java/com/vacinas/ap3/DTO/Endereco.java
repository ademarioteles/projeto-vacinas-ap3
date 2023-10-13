package com.vacinas.ap3.DTO;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
@Data
public class Endereco {
    private String logradouro;
    private Integer numero;
    private String bairro;
    private String municipio;
    private String estado;
}
