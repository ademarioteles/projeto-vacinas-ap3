package com.vacinas.ap3.util;

import com.vacinas.ap3.DTO.Vacina;

public class VacinaUtils {

    public static Vacina criarVacinaExemplo() {
        Vacina vacinaExemplo = new Vacina();
        vacinaExemplo.setId("1");
        vacinaExemplo.setNome("VacinaX");
        vacinaExemplo.setFabricante("FabricanteY");
        vacinaExemplo.setLote("Lote123");
        vacinaExemplo.setData_validade("2024-12-31");
        vacinaExemplo.setNumero_de_doses(2);
        vacinaExemplo.setIntervalo_doses(21);
        return vacinaExemplo;
    }

    public static Vacina criarOutraVacina() {
        Vacina outraVacina = new Vacina();
        outraVacina.setId("2");
        outraVacina.setNome("VacinaZ");
        outraVacina.setFabricante("FabricanteW");
        outraVacina.setLote("Lote456");
        outraVacina.setData_validade("2025-10-15");
        outraVacina.setNumero_de_doses(2);
        outraVacina.setIntervalo_doses(28);
        return outraVacina;
    }
}
