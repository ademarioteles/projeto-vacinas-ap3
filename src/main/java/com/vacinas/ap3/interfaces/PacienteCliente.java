package com.vacinas.ap3.interfaces;
import com.vacinas.ap3.DTO.Paciente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "api2", url = "http://localhost:8081")
public interface PacienteCliente {

    @GetMapping("/pacientes")
    List<Paciente> listarPacientes();

    @GetMapping("/pacientes/{id}")
    Paciente Paciente(@PathVariable String id);
}
