package com.vacinas.ap3.service;
import com.vacinas.ap3.DTO.Paciente;
import com.vacinas.ap3.client.PacienteCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Service
public class ClientPacientesService {
    private final PacienteCliente client;

    @Autowired
    public ClientPacientesService(PacienteCliente client) {
        this.client = client;
    }
    @ResponseBody
    public ResponseEntity <List<Paciente>> listarPacientesDaApi2() {
        return client.listarPacientes();
    }
    @ResponseBody
    public ResponseEntity PacienteDaApi2(String id) {
        return client.Paciente(id);
    }
}
