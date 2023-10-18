package com.vacinas.ap3.entity;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Document
@Data
@RequiredArgsConstructor
public class RegistroDeVacinacao {
    @Id
    private String id;
    @NotNull(message = "A data de vacinação não pode estar nulo.")
    @NotEmpty(message = "A data de vacinação não pode estar vazio.")
    private LocalDate dataDeVacinacao;
    @NotNull(message = "A Identificação do paciente não pode estar nulo.")
    @NotEmpty(message = "A Identificação do paciente não pode estar vazio.")
    private String identificacaoDoPaciente; // Pode ser o ID do paciente ou outro identificador único
    @NotNull(message = "A Identificação da vacina não pode estar nulo.")
    @NotEmpty(message = "A Identificação da vacina não pode estar vazio.")
    private String identificacaoDaVacina; // Pode ser o ID da vacina ou outro identificador único
    @NotNull(message = "A Identificação da dose não pode estar nulo.")
    @NotEmpty(message = "A Identificação da dose não pode estar vazio.")
    private int identificacaoDaDose;
    @Valid
    private ProfissionalDeSaude profissionalDeSaude;
}
