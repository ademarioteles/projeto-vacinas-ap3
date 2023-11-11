package com.vacinas.ap3.util;

import com.vacinas.ap3.DTO.Endereco;
import com.vacinas.ap3.DTO.Paciente;
import com.vacinas.ap3.DTO.Vacina;

import java.util.ArrayList;
import java.util.List;

public class PacienteUtils {

    public static Paciente criarUmPaciente() {
        Paciente paciente = new Paciente();
        paciente.setId("1");
        paciente.setNome("Fulano");
        paciente.setSobrenome("Silva");
        paciente.setCpf("12345678900");
        paciente.setDataNascimento("1990-01-01");
        paciente.setSexo("M");
        paciente.setContato("fulano@example.com");

        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua A");
        endereco.setNumero(100);
        endereco.setBairro("Bairro A");
        endereco.setMunicipio("São Paulo");
        endereco.setEstado("SP");
        paciente.setEndereco(endereco);

        return paciente;
    }

    public static Paciente criarOutroPaciente() {
        Paciente paciente = new Paciente();
        paciente.setId("2");
        paciente.setNome("Ciclano");
        paciente.setSobrenome("Santos");
        paciente.setCpf("98765432100");
        paciente.setDataNascimento("1985-05-05");
        paciente.setSexo("F");
        paciente.setContato("ciclano@example.com");

        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua B");
        endereco.setNumero(200);
        endereco.setBairro("Bairro B");
        endereco.setMunicipio("Rio de Janeiro");
        endereco.setEstado("RJ");
        paciente.setEndereco(endereco);

        return paciente;
    }

    public static List<Paciente> criarListaPacientesExemplo() {
        List<Paciente> pacientes = new ArrayList<>();
        pacientes.add(criarUmPaciente());
        pacientes.add(criarOutroPaciente());
        return pacientes;
    }
}
