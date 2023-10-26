package com.vacinas.ap3.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vacinas.ap3.DTO.Endereco;
import com.vacinas.ap3.DTO.Paciente;
import com.vacinas.ap3.DTO.Vacina;
import com.vacinas.ap3.entity.Mensagem;
import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.exceptions.*;
import com.vacinas.ap3.repository.RegistroDeVacinacaoRepository;
import feign.FeignException;
import feign.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class RegistroDeVacinacaoService {
    private RegistroDeVacinacaoRepository registroDeVacinacaoRepository;
    private InterfaceAPI2Service interfaceAPI2Service;
    private InterfaceAPI1Service interfaceAPI1Service;

    public RegistroDeVacinacaoService(RegistroDeVacinacaoRepository registroDeVacinacaoRepository, InterfaceAPI2Service interfaceAPI2Service, InterfaceAPI1Service interfaceAPI1Service) {
        this.registroDeVacinacaoRepository = registroDeVacinacaoRepository;
        this.interfaceAPI2Service = interfaceAPI2Service;
        this.interfaceAPI1Service = interfaceAPI1Service;
    }
    private void validarPacienteExistente(String identificacaoDoPaciente) {
        Response response;
       // try {
            response = (Response) interfaceAPI2Service.PacienteDaApi2(identificacaoDoPaciente);
            System.out.println(response.status());
            ResponseEntity <Object>  reposta = (ResponseEntity) interfaceAPI2Service.PacienteDaApi2(identificacaoDoPaciente);
            System.out.println(reposta);
      /*  } catch (FeignException e) {
            // Use regex para extrair a parte JSON da string
            String jsonPart = e.getMessage().replaceAll(".*?\\[.*?\\]: ", "");
            // Parse o JSON
            JsonObject jsonObject = JsonParser.parseString(jsonPart).getAsJsonObject();

            // Acesse a mensagem
            String mensagem = jsonObject.get("messagem").getAsString();
                throw new ExteriorException("aaaaa");
        } */
    }

    private void validarVacinaExistente(String identificacaoDaVacina) {
        List<Vacina> vacinas = interfaceAPI1Service.listarVacinasDaApi1();
        boolean vacinaExiste = vacinas.stream()
                .anyMatch(vacina -> vacina.getId().equals(identificacaoDaVacina));
        if (!vacinaExiste) {
            throw new VacinaInexistenteException("A vacina não encontrada");
        }
    }

    private void validarDose(RegistroDeVacinacao registro, List<RegistroDeVacinacao> registros) {
        List<Vacina> vacinas = interfaceAPI1Service.listarVacinasDaApi1();
        for (RegistroDeVacinacao registroExistente : registros) {
            if (registroExistente.getIdentificacaoDaDose() == registro.getIdentificacaoDaDose()) {
                throw new RegistroExistenteException("Registro de vacinação já existe");
            }
        }

        Vacina vacinaAplicada = vacinas.stream()
                .filter(vacina -> vacina.getId().equals(registro.getIdentificacaoDaVacina()))
                .findFirst()
                .orElseThrow(() -> new VacinaInexistenteException("A vacina não encontrada"));

        if (registro.getIdentificacaoDaDose() > vacinaAplicada.getNumero_de_doses()) {
            throw new DoseMaiorException("Número de dose maior que o permitido");
        }

        LocalDate dataUltimaDose = registros.stream()
                .map(RegistroDeVacinacao::getDataDeVacinacao)
                .max(Comparator.naturalOrder())
                .orElse(LocalDate.MIN);

        LocalDate dataRegistroAtual = registro.getDataDeVacinacao();
        long intervaloDias = ChronoUnit.DAYS.between(dataUltimaDose, dataRegistroAtual);

      /*  if (intervaloDias < vacinaAplicada.getIntervalo_doses()) {
            throw new IntervaloInsuficienteException("Intervalo insuficiente entre doses");
        }
        if (!registro.getIdentificacaoDaVacina().equals(registros.get(0).getIdentificacaoDaVacina())) {
            throw new VacinaIncompativelException("Vacina diferente das doses anteriores");
        }*/
    }

    public ResponseEntity criarRegistroDeVacinacao(RegistroDeVacinacao registroDeVacinacao) {
        try {
            validarPacienteExistente(registroDeVacinacao.getIdentificacaoDoPaciente());
            validarVacinaExistente(registroDeVacinacao.getIdentificacaoDaVacina());

            List<RegistroDeVacinacao> registros = obterRegistroDeVacinacaoPorIdDoPaciente(registroDeVacinacao.getIdentificacaoDoPaciente());
            validarDose(registroDeVacinacao, registros);

            registroDeVacinacaoRepository.save(registroDeVacinacao);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Mensagem("Registro cadastrado com sucesso!"));
        } catch (PacienteInexistenteException | VacinaInexistenteException | DoseMaiorException |
                 RegistroExistenteException | VacinaIncompativelException | IntervaloInsuficienteException e) {
            // Lidar com exceções
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Mensagem("Erro: " + e.getMessage()));
        }
    }

    public List<RegistroDeVacinacao> listarTodosOsRegistrosDeVacinacao() {
        return registroDeVacinacaoRepository.findAll();
    }

    public List<RegistroDeVacinacao> obterRegistrosDeVacinacaoPorIdDaVacina(String id) {
        List<RegistroDeVacinacao> listaRegistros = listarTodosOsRegistrosDeVacinacao();
        return listaRegistros.stream()
                .filter(registro -> registro.getIdentificacaoDaVacina().equals(id))
                .collect(Collectors.toList());
    }

    public Object obterRegistroResumidoDeVacinacaoPorIdDoPaciente(String id) {
        Response pacienteResponse = (Response) interfaceAPI2Service.PacienteDaApi2(id);
        Paciente paciente = (Paciente) pacienteResponse.body();
        Endereco endereco = paciente.getEndereco();
        List<RegistroDeVacinacao> listaRegistros = listarTodosOsRegistrosDeVacinacao().stream()
                .filter(registro -> registro.getIdentificacaoDoPaciente().equals(id))
                .collect(Collectors.toList());
        List<Vacina> vacinas = interfaceAPI1Service.listarVacinasDaApi1();
        Optional<Vacina> vacinaOptional = vacinas.stream()
                .filter(vacinaFilter -> vacinaFilter.getId().equals(listaRegistros.get(0).getIdentificacaoDaVacina()))
                .findFirst();
        Vacina vacina = vacinaOptional.get();

        Map<String, Object> dados = new HashMap<>();

        Map<String, Object> jPaciente = new HashMap<>();
        jPaciente.put("nome", paciente.getNome());
        jPaciente.put("idade", 58);
        jPaciente.put("bairro", endereco.getBairro());
        jPaciente.put("municipio", endereco.getMunicipio());
        jPaciente.put("estado", endereco.getEstado());

        Map<String, Object> jVacina = new HashMap<>();
        jVacina.put("fabricante", vacina.getFabricante());
        jVacina.put("vacina", vacina.getNome());
        jVacina.put("total_de_doses", vacina.getNumero_de_doses());
        jVacina.put("intervalo_entre_doses", vacina.getIntervalo_doses());

        List<String> jDoses = new ArrayList<>();
        for (RegistroDeVacinacao registro : listaRegistros){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            jDoses.add(registro.getDataDeVacinacao().format(formatter));
        }

        dados.put("doses", jDoses);
        dados.put("vacina", jVacina);
        dados.put("paciente", jPaciente);

        Gson gson = new Gson();
        String json = gson.toJson(dados);
        return gson.fromJson(json, Object.class);
    }

    public Integer obterNumeroDeVacinacao(String estado) {
        if (estado != null) {
            // Se o parâmetro "estado" estiver presente, retorne o total de vacinas aplicadas para esse estado
            ResponseEntity pacientesResponse = interfaceAPI2Service.listarPacientesDaApi2();
            List<Paciente> pacientes = (List<Paciente>) pacientesResponse.getBody();
            List<RegistroDeVacinacao> listaRegistros = listarTodosOsRegistrosDeVacinacao();
            Map<String, Long> pacienteParaQuantidade = listaRegistros.stream()
                    .collect(Collectors.groupingBy(RegistroDeVacinacao::getIdentificacaoDoPaciente, Collectors.counting()));
            Integer quantVacinacaoEstado = 0;
            for (Paciente paciente : pacientes) {
                if (estado.equals(paciente.getEndereco().getEstado())) {
                    String identificacaoPaciente = paciente.getId();
                    Long quantidadeDeRegistros = pacienteParaQuantidade.get(identificacaoPaciente);

                    if (quantidadeDeRegistros != null) {
                        quantVacinacaoEstado += quantidadeDeRegistros.intValue();
                    }
                }
            }
            return quantVacinacaoEstado;
        } else {
            // Caso contrário, retorne o total de vacinas aplicadas de forma geral
            return listarTodosOsRegistrosDeVacinacao().size();
        }
    }

    public List<RegistroDeVacinacao> obterRegistroDeVacinacaoPorIdDoPaciente(String id) {
        List<RegistroDeVacinacao> listaRegistros = listarTodosOsRegistrosDeVacinacao();

        return listaRegistros.stream()
                .filter(registro -> registro.getIdentificacaoDoPaciente().equals(id))
                .collect(Collectors.toList());
    }
}