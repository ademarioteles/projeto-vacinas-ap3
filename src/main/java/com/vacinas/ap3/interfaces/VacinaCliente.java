package com.vacinas.ap3.interfaces;
import com.vacinas.ap3.DTO.Paciente;
import com.vacinas.ap3.DTO.Vacina;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "api1", url = "http://localhost:8080")
public interface VacinaCliente {

    @GetMapping("/vacinas")
    List<Vacina> listarVacinas();
}
