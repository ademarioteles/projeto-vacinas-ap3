package com.vacinas.ap3.service;
import com.vacinas.ap3.DTO.Vacina;
import com.vacinas.ap3.client.VacinaCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterfaceAPI1Service {
    private final VacinaCliente api1Client;

    @Autowired
    public InterfaceAPI1Service(VacinaCliente api1Client) {
        this.api1Client = api1Client;
    }

    public ResponseEntity <List<Vacina>> listarVacinasDaApi1() {
        return api1Client.listarVacinas();
    }

    public ResponseEntity  buscarVacinaDaApi1(String id) {
        return api1Client.vacina(id);
    }
}
