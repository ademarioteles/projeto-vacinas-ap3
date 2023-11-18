package com.vacinas.ap3.interfaces;
import com.vacinas.ap3.DTO.Paciente;
import com.vacinas.ap3.DTO.Vacina;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "api1", url = "https://20.119.136.3:8080")
public interface VacinaCliente {

    @GetMapping("/vacinas")
    ResponseEntity <List<Vacina>> listarVacinas();

    @GetMapping("/vacinas/{id}")
    ResponseEntity <Vacina> vacina(@PathVariable String id);
}
