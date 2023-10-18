package com.vacinas.ap3.DTO;

import lombok.Data;

@Data
public class Vacina {
    private String id;

    private String nome;

    private String fabricante;

    private String lote;

    private String data_validade;

    private Integer numero_de_doses;

    private Integer intervalo_doses;
}
