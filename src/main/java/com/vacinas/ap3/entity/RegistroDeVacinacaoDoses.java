package com.vacinas.ap3.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RegistroDeVacinacaoDoses {
    private String fabricante;
    private String vacina;
    private Integer dosesAplicadas;

}
