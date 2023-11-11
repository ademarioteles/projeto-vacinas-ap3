package com.vacinas.ap3.util;

import com.vacinas.ap3.entity.ProfissionalDeSaude;
import com.vacinas.ap3.entity.RegistroDeVacinacao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RegistroDeVacinacaoUtils {

    public static RegistroDeVacinacao criarRegistroDeVacinacaoExemplo() {
        RegistroDeVacinacao registro = new RegistroDeVacinacao();
        ProfissionalDeSaude profissionalDeSaude = new ProfissionalDeSaude();
        registro.setId("1");
        registro.setDataDeVacinacao(LocalDate.now());
        registro.setIdentificacaoDoPaciente("6539cce426ac996a47e0ce8d");
        registro.setIdentificacaoDaVacina("652f344fe8be16628ceb8f0b");
        registro.setIdentificacaoDaDose(1);
        profissionalDeSaude.setNome("Maria");
        profissionalDeSaude.setCpf("2345678");
        registro.setProfissionalDeSaude(profissionalDeSaude);
        return registro;
    }

    public static List<RegistroDeVacinacao> criarListaRegistrosExemplo() {
        List<RegistroDeVacinacao> registros = new ArrayList<>();
        ProfissionalDeSaude profissionalDeSaude = new ProfissionalDeSaude();
        RegistroDeVacinacao registro = new RegistroDeVacinacao();
        registro.setId("1");
        registro.setDataDeVacinacao(LocalDate.now());
        registro.setIdentificacaoDoPaciente("6539cce426ac996a47e0ce8d");
        registro.setIdentificacaoDaVacina("652f344fe8be16628ceb8f0b");
        registro.setIdentificacaoDaDose(1);
        profissionalDeSaude.setNome("Maria");
        profissionalDeSaude.setCpf("2345678");
        registro.setProfissionalDeSaude(profissionalDeSaude);
        registros.add(registro);
        //segunda dose
        registro.setId("2");
        registro.setDataDeVacinacao(LocalDate.now().plusDays(1));
        registro.setIdentificacaoDoPaciente("6539cce426ac996a47e0ce8d");
        registro.setIdentificacaoDaVacina("652f344fe8be16628ceb8f0b");
        registro.setIdentificacaoDaDose(2);
        profissionalDeSaude.setNome("Maria");
        profissionalDeSaude.setCpf("2345678");
        registro.setProfissionalDeSaude(profissionalDeSaude);
        registros.add(registro);
        //segunda dose
        registro.setId("3");
        registro.setDataDeVacinacao(LocalDate.now().plusDays(2));
        registro.setIdentificacaoDoPaciente("6539cce426ac996a47e0ce8d");
        registro.setIdentificacaoDaVacina("652f344fe8be16628ceb8f0b");
        registro.setIdentificacaoDaDose(3);
        profissionalDeSaude.setNome("Maria");
        profissionalDeSaude.setCpf("2345678");
        registro.setProfissionalDeSaude(profissionalDeSaude);
        registros.add(registro);

        return registros;
    }
}
