package com.vacinas.ap3.controller;

import com.vacinas.ap3.service.InterfaceAPI2Service;
import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pacientes")
public class Pacientes {
    RegistroDeVacinacaoService registroDeVacinacaoService;
    private InterfaceAPI2Service interfaceAPI2Service;

    public Pacientes(RegistroDeVacinacaoService registroDeVacinacaoService, InterfaceAPI2Service interfaceAPI2Service) {
        this.registroDeVacinacaoService = registroDeVacinacaoService;
        this.interfaceAPI2Service = interfaceAPI2Service;
    }

    @GetMapping("/{id}/vacinas")
    public Object obterRegistroResumidoDeVacinacaoPorIdDoPaciente(@PathVariable String id) {
            return registroDeVacinacaoService.obterRegistroResumidoDeVacinacaoPorIdDoPaciente(id);
    }

    /*@GetMapping("/vacinas")
    public ResponseEntity<List<Paciente>> obterPacientesAtrasados(@RequestParam(name = "estado", required = false) String estado) {
        List<RegistroDeVacinacao> registros = registroDeVacinacaoService.listarTodosOsRegistrosDeVacinacao();
        List<Paciente> pacientes = null;

        LocalDate dataAtual = LocalDate.now();

        List<RegistroDeVacinacao> registrosComDosesAtrasadas = registros.stream()
                .filter(registro -> {
                    // Calcula a data alvo com base na data da última dose e no intervalo entre doses
                    LocalDate dataAlvo = registro.getDataDaUltimaDose().plusDays(registro.getVacina().getIntervaloEntreDoses());
                    return dataAlvo.isBefore(dataAtual);
                })
                .collect(Collectors.toList());

        List<Paciente> pacientesComDosesAtrasadas = registrosComDosesAtrasadas.stream()
                .map(RegistroDeVacinacao::getPaciente)
                .collect(Collectors.toList());

        for (Paciente paciente : pacientesComDosesAtrasadas){
            pacientes.add(interfaceAPI2Service.PacienteDaApi2(paciente.getId()));
        }
        if (pacientes == null){
            throw new PacienteInexistenteException("Pacientes não encontrados");
        }else{
            return ResponseEntity.status(200).body(pacientes);
        }

    }*/
}
