package com.vacinas.ap3.service;
import com.vacinas.ap3.DTO.Vacina;
import com.vacinas.ap3.client.VacinaCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientVacinasService {
    private final VacinaCliente cliente;

    @Autowired
    public ClientVacinasService(VacinaCliente cliente) {
        this.cliente = cliente;
    }

    public ResponseEntity <List<Vacina>> listarVacinas() {
        return cliente.listarVacinas();
    }

    public ResponseEntity buscarVacina(String id) {
        return cliente.vacina(id);
    }
}
