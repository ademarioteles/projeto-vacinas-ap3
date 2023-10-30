package com.vacinas.ap3.entity;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document
@Data
@RequiredArgsConstructor
public class RegistroDeVacinacaoResumido {
    private String nome;
    private Integer idade;
    private String bairro;
    private String municipio;
    private String estado;
    private String fabricanteVacina;
    private String nomeVacina;
    private Integer totalDeDosesVacina;
    private Integer intervaloEntreDoses;
    private List<String> doses;
}
