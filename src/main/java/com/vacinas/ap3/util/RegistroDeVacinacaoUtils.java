package com.vacinas.ap3.util;

import com.vacinas.ap3.entity.ProfissionalDeSaude;
import com.vacinas.ap3.entity.RegistroDeVacinacao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public static RegistroDeVacinacao criarOutroRegistroDeVacinacaoExemplo() {
        RegistroDeVacinacao registro = new RegistroDeVacinacao();
        ProfissionalDeSaude profissionalDeSaude = new ProfissionalDeSaude();
        registro.setId("1");
        registro.setDataDeVacinacao(LocalDate.now().minusDays(1));
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
        RegistroDeVacinacao registro2 = new RegistroDeVacinacao();
        registro2.setId("2");
        registro2.setDataDeVacinacao(LocalDate.now().plusDays(1));
        registro2.setIdentificacaoDoPaciente("6539cce426ac996a47e0ce8d");
        registro2.setIdentificacaoDaVacina("652f344fe8be16628ceb8f0b");
        registro2.setIdentificacaoDaDose(2);
        profissionalDeSaude.setNome("Maria");
        profissionalDeSaude.setCpf("2345678");
        registro2.setProfissionalDeSaude(profissionalDeSaude);
        registros.add(registro2);
        //terceira dose
        RegistroDeVacinacao registro3 = new RegistroDeVacinacao();
        registro3.setId("3");
        registro3.setDataDeVacinacao(LocalDate.now().plusDays(2));
        registro3.setIdentificacaoDoPaciente("6539cce426ac996a47e0ce8d");
        registro3.setIdentificacaoDaVacina("652f344fe8be16628ceb8f0b");
        registro3.setIdentificacaoDaDose(3);
        profissionalDeSaude.setNome("Maria");
        profissionalDeSaude.setCpf("2345678");
        registro3.setProfissionalDeSaude(profissionalDeSaude);
        registros.add(registro3);

        return registros;
    }
    public static List<RegistroDeVacinacao> criarOutraListaRegistrosExemploP1() {
        List<RegistroDeVacinacao> registros = new ArrayList<>();
        ProfissionalDeSaude profissionalDeSaude = new ProfissionalDeSaude();
        RegistroDeVacinacao registro = new RegistroDeVacinacao();
        registro.setId("1");
        registro.setDataDeVacinacao(LocalDate.now());
        registro.setIdentificacaoDoPaciente("1");
        registro.setIdentificacaoDaVacina("1");
        registro.setIdentificacaoDaDose(1);
        profissionalDeSaude.setNome("Maria");
        profissionalDeSaude.setCpf("2345678");
        registro.setProfissionalDeSaude(profissionalDeSaude);
        registros.add(registro);
        //segunda dose
        RegistroDeVacinacao registro1 = new RegistroDeVacinacao();
        registro1.setId("2");
        registro1.setDataDeVacinacao(LocalDate.now().plusDays(1));
        registro1.setIdentificacaoDoPaciente("1");
        registro1.setIdentificacaoDaVacina("1");
        registro1.setIdentificacaoDaDose(2);
        profissionalDeSaude.setNome("Maria");
        profissionalDeSaude.setCpf("2345678");
        registro1.setProfissionalDeSaude(profissionalDeSaude);
        registros.add(registro1);
        //terceira dose
        RegistroDeVacinacao registro2 = new RegistroDeVacinacao();
        registro2.setId("3");
        registro2.setDataDeVacinacao(LocalDate.now().plusDays(2));
        registro2.setIdentificacaoDoPaciente("1");
        registro2.setIdentificacaoDaVacina("1");
        registro2.setIdentificacaoDaDose(3);
        profissionalDeSaude.setNome("Maria");
        profissionalDeSaude.setCpf("2345678");
        registro.setProfissionalDeSaude(profissionalDeSaude);
        registros.add(registro2);

        return registros;
    }
    public static List<RegistroDeVacinacao> criarOutraListaRegistrosExemploP2() {
        List<RegistroDeVacinacao> registros = new ArrayList<>();
        ProfissionalDeSaude profissionalDeSaude = new ProfissionalDeSaude();
        RegistroDeVacinacao registro = new RegistroDeVacinacao();
        registro.setId("1");
        registro.setDataDeVacinacao(LocalDate.now());
        registro.setIdentificacaoDoPaciente("2");
        registro.setIdentificacaoDaVacina("1");
        registro.setIdentificacaoDaDose(1);
        profissionalDeSaude.setNome("Maria");
        profissionalDeSaude.setCpf("2345678");
        registro.setProfissionalDeSaude(profissionalDeSaude);
        registros.add(registro);
        //segunda dose
        RegistroDeVacinacao registro1 = new RegistroDeVacinacao();
        registro1.setId("2");
        registro1.setDataDeVacinacao(LocalDate.now().plusDays(1));
        registro1.setIdentificacaoDoPaciente("2");
        registro1.setIdentificacaoDaVacina("1");
        registro1.setIdentificacaoDaDose(2);
        profissionalDeSaude.setNome("Maria");
        profissionalDeSaude.setCpf("2345678");
        registro1.setProfissionalDeSaude(profissionalDeSaude);
        registros.add(registro1);


        return registros;
    }
    public static List<RegistroDeVacinacao> injectDados() {
        List<RegistroDeVacinacao> registros = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //Registro COMPLETO, Com todas as doses de vacinação (3 doses)
        RegistroDeVacinacao registro = new RegistroDeVacinacao();
        ProfissionalDeSaude profissionalDeSaude = new ProfissionalDeSaude();
        registro.setId("1");
        registro.setDataDeVacinacao(LocalDate.parse("2021-01-01", formatter));
        registro.setIdentificacaoDoPaciente("6556b65c2ba8c674fd37b804");
        registro.setIdentificacaoDaVacina("65582566c691757a205e3302");
        registro.setIdentificacaoDaDose(1);
        profissionalDeSaude.setNome("Maria");
        profissionalDeSaude.setCpf("037.529.400-77");
        registro.setProfissionalDeSaude(profissionalDeSaude);
        registros.add(registro);
        //segunda dose
        RegistroDeVacinacao registro2 = new RegistroDeVacinacao();
        ProfissionalDeSaude profissionalDeSaude2 = new ProfissionalDeSaude();
        registro2.setId("2");
        registro2.setDataDeVacinacao(LocalDate.parse("2021-02-01", formatter));
        registro2.setIdentificacaoDoPaciente("6556b65c2ba8c674fd37b804");
        registro2.setIdentificacaoDaVacina("65582566c691757a205e3302");
        registro2.setIdentificacaoDaDose(2);
        profissionalDeSaude2.setNome("Maria");
        profissionalDeSaude2.setCpf("037.529.400-77");
        registro2.setProfissionalDeSaude(profissionalDeSaude2);
        registros.add(registro2);
        //teceira dose
        RegistroDeVacinacao registro3 = new RegistroDeVacinacao();
        ProfissionalDeSaude profissionalDeSaude3 = new ProfissionalDeSaude();
        registro3.setId("3");
        registro3.setDataDeVacinacao(LocalDate.parse("2021-03-01", formatter));
        registro3.setIdentificacaoDoPaciente("6556b65c2ba8c674fd37b804");
        registro3.setIdentificacaoDaVacina("65582566c691757a205e3302");
        registro3.setIdentificacaoDaDose(3);
        profissionalDeSaude3.setNome("Maria");
        profissionalDeSaude3.setCpf("037.529.400-77");
        registro3.setProfissionalDeSaude(profissionalDeSaude3);
        registros.add(registro3);
        // Fim registro completo
        //Registro COMPLETO 2, Com todas as doses de vacinação (4 doses)
        RegistroDeVacinacao registro4 = new RegistroDeVacinacao();
        ProfissionalDeSaude profissionalDeSaude4 = new ProfissionalDeSaude();
        registro4.setId("4");
        registro4.setDataDeVacinacao(LocalDate.parse("2021-01-01", formatter));
        registro4.setIdentificacaoDoPaciente("6556b65c2ba8c674fd37b803");
        registro4.setIdentificacaoDaVacina("65582566c691757a205e3303");
        registro4.setIdentificacaoDaDose(1);
        profissionalDeSaude4.setNome("Anastacia");
        profissionalDeSaude4.setCpf("537.356.340-44");
        registro4.setProfissionalDeSaude(profissionalDeSaude4);
        registros.add(registro4);
        //segunda dose
        RegistroDeVacinacao registro5 = new RegistroDeVacinacao();
        ProfissionalDeSaude profissionalDeSaude5 = new ProfissionalDeSaude();
        registro5.setId("5");
        registro5.setDataDeVacinacao(LocalDate.parse("2021-02-01", formatter));
        registro5.setIdentificacaoDoPaciente("6556b65c2ba8c674fd37b803");
        registro5.setIdentificacaoDaVacina("65582566c691757a205e3303");
        registro5.setIdentificacaoDaDose(2);
        profissionalDeSaude5.setNome("Anastacia");
        profissionalDeSaude5.setCpf("537.356.340-44");
        registro5.setProfissionalDeSaude(profissionalDeSaude5);
        registros.add(registro5);
        //teceira dose
        RegistroDeVacinacao registro6 = new RegistroDeVacinacao();
        ProfissionalDeSaude profissionalDeSaude6 = new ProfissionalDeSaude();
        registro6.setId("6");
        registro6.setDataDeVacinacao(LocalDate.parse("2021-03-01", formatter));
        registro6.setIdentificacaoDoPaciente("6556b65c2ba8c674fd37b803");
        registro6.setIdentificacaoDaVacina("65582566c691757a205e3303");
        registro6.setIdentificacaoDaDose(3);
        profissionalDeSaude6.setNome("Anastacia");
        profissionalDeSaude6.setCpf("537.356.340-44");
        registro6.setProfissionalDeSaude(profissionalDeSaude6);
        registros.add(registro6);
        //quarta dose
        RegistroDeVacinacao registro7 = new RegistroDeVacinacao();
        ProfissionalDeSaude profissionalDeSaude7 = new ProfissionalDeSaude();
        registro7.setId("7");
        registro7.setDataDeVacinacao(LocalDate.parse("2021-04-01", formatter));
        registro7.setIdentificacaoDoPaciente("6556b65c2ba8c674fd37b803");
        registro7.setIdentificacaoDaVacina("65582566c691757a205e3303");
        registro7.setIdentificacaoDaDose(4);
        profissionalDeSaude7.setNome("Anastacia");
        profissionalDeSaude7.setCpf("537.356.340-44");
        registro7.setProfissionalDeSaude(profissionalDeSaude7);
        registros.add(registro7);
        // Fim registro completo 2
        //Registro atrasado, Estado BA
        RegistroDeVacinacao registro8 = new RegistroDeVacinacao();
        ProfissionalDeSaude profissionalDeSaude8 = new ProfissionalDeSaude();
        registro8.setId("8");
        registro8.setDataDeVacinacao(LocalDate.parse("2021-01-01", formatter));
        registro8.setIdentificacaoDoPaciente("6556b65c2ba8c674fd37b703");
        registro8.setIdentificacaoDaVacina("65582566c691757a205e3304");
        registro8.setIdentificacaoDaDose(1);
        profissionalDeSaude8.setNome("Joao");
        profissionalDeSaude8.setCpf("167.276.390-86");
        registro8.setProfissionalDeSaude(profissionalDeSaude8);
        registros.add(registro8);
        //Registro atrasado, Estado SP
        RegistroDeVacinacao registro9 = new RegistroDeVacinacao();
        ProfissionalDeSaude profissionalDeSaude9 = new ProfissionalDeSaude();
        registro9.setId("9");
        registro9.setDataDeVacinacao(LocalDate.parse("2021-01-01", formatter));
        registro9.setIdentificacaoDoPaciente("6556b65c2ba8c674fd37b712");
        registro9.setIdentificacaoDaVacina("65582566c691757a205e3306");
        registro9.setIdentificacaoDaDose(1);
        profissionalDeSaude9.setNome("Carlos");
        profissionalDeSaude9.setCpf("408.406.800-40");
        registro9.setProfissionalDeSaude(profissionalDeSaude9);
        registros.add(registro9);
        //Fim registro atrasado
        return registros;
    }
}
