package com.vacinas.ap3.exceptions;

import com.vacinas.ap3.entity.Mensagem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GenericHandlerException extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericHandlerException.class);

    @ExceptionHandler(ExteriorException.class)
    protected ResponseEntity handleException(ExteriorException e) {
        Mensagem mensagem = new Mensagem("exceção externa!");
        LOGGER.info("Tratamentação de exceção externa: " + mensagem);
        return new ResponseEntity(mensagem, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RegistroExistenteException.class)
    protected ResponseEntity handleException(RegistroExistenteException e) {
        Mensagem mensagem = new Mensagem("O registro de vacinacão já encontra-se cadastrado em nossa base de dados!");
        LOGGER.info("Tratamentação de exceção RegistroExistenteException: " + mensagem);
        return new ResponseEntity(mensagem, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(PacienteInexistenteException.class)
    protected ResponseEntity handleException(PacienteInexistenteException e) {
        Mensagem mensagem = new Mensagem("Paciente não encontrado");
        LOGGER.info("Tratamentação de exceção PacienteInexistenteException: " + mensagem);
        return new ResponseEntity(mensagem, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VacinaInexistenteException.class)
    protected ResponseEntity handleException(VacinaInexistenteException e) {
        Mensagem mensagem = new Mensagem("Vacina não encontrado");
        LOGGER.info("Tratamentação de exceção VacinaInexistenteException: " + mensagem);
        return new ResponseEntity(mensagem, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IntervaloInsuficienteException.class)
    protected ResponseEntity handleException(IntervaloInsuficienteException e) {
        Mensagem mensagem = new Mensagem("Intervalo de vacinação insuficiente");
        LOGGER.info("Tratamentação de exceção IntervaloInsuficienteException: " + mensagem);
        return new ResponseEntity(mensagem, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VacinaIncompativelException.class)
    protected ResponseEntity handleException(VacinaIncompativelException e) {
        Mensagem mensagem = new Mensagem("Vacina incompatível com registros anteriores");
        LOGGER.info("Tratamentação de exceção VacinaIncompativelException: " + mensagem);
        return new ResponseEntity(mensagem, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DoseMaiorException.class)
    protected ResponseEntity handleException(DoseMaiorException e) {
        Mensagem mensagem = new Mensagem("Dose maior que permitido");
        LOGGER.info("Tratamentação de exceção DoseMaiorException: " + mensagem);
        return new ResponseEntity(mensagem, HttpStatus.NOT_FOUND);
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<Mensagem> message = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            message.add(new Mensagem(error.getDefaultMessage()));
            LOGGER.info("Tratamento de Exceção MethodArgumentNotValidException: " + error.getDefaultMessage());
        }
        return handleExceptionInternal(ex, message, headers, HttpStatus.BAD_REQUEST, request);
    }
}
