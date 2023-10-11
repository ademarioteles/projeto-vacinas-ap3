package com.vacinas.ap3.entity;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@RequiredArgsConstructor
public class RegistroDeVacinacao {
    @Id
    private String id;
    private String dataDeVacinação;
    private String identificacaoDoPaciente; // Pode ser o ID do paciente ou outro identificador único
    private String identificacaoDaVacina; // Pode ser o ID da vacina ou outro identificador único
    private int identificacaoDaDose;
    private ProfissionalDeSaude profissionalDeSaude;
}
