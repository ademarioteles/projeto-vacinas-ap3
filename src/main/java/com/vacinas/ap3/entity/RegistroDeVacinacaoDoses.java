package com.vacinas.ap3.entity;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@RequiredArgsConstructor
public class RegistroDeVacinacaoDoses {
    private String fabricante;
    private String vacina;
    private Integer dosesAplicadas;

}
