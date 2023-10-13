package com.vacinas.ap3.service;
import com.vacinas.ap3.DTO.Paciente;
import com.vacinas.ap3.interfaces.PacienteCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterfaceAPI2Service {
    private final PacienteCliente api2Client;

    @Autowired
    public InterfaceAPI2Service(PacienteCliente api2Client) {
        this.api2Client = api2Client;
    }

    public List<Paciente> listarPacientesDaApi2() {
        return api2Client.listarPacientes();
    }
}
