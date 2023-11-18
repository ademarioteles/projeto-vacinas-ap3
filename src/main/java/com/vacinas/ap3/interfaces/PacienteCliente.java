package com.vacinas.ap3.interfaces;
import com.vacinas.ap3.DTO.Paciente;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "api2", url = "https://20.119.136.3:8081")
public interface PacienteCliente {

    @GetMapping("/pacientes")
    ResponseEntity <List<Paciente>> listarPacientes();

    @GetMapping("/pacientes/{id}")
    ResponseEntity <Paciente> Paciente(@PathVariable String id);
}
