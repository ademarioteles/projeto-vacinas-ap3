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
        Mensagem mensagem = new Mensagem(e.getMessage());
        LOGGER.info("Tratamentação de exceção externa: " + mensagem);
        return new ResponseEntity(mensagem, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DataBaseException.class)
    protected ResponseEntity handleException(DataBaseException e) {
        Mensagem mensagem = new Mensagem(e.getMessage());
        LOGGER.info("Tratamentação de exceção banco de dados: " + mensagem);
        return new ResponseEntity(mensagem, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RegistroExistenteException.class)
    protected ResponseEntity handleException(RegistroExistenteException e) {
        Mensagem mensagem = new Mensagem(e.getMessage());
        LOGGER.info("Tratamentação de exceção RegistroExistenteException: " + mensagem);
        return new ResponseEntity(mensagem, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RegistroInexistenteException.class)
    protected ResponseEntity handleException(RegistroInexistenteException e) {
        Mensagem mensagem = new Mensagem(e.getMessage());
        LOGGER.info("Tratamentação de exceção RegistroInexistenteException: " + mensagem);
        return new ResponseEntity(mensagem, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ErroCriacaoRegistro.class)
    protected ResponseEntity handleException(ErroCriacaoRegistro e) {
        Mensagem mensagem = new Mensagem(e.getMessage());
        LOGGER.info("Tratamentação de exceção ErroCriacaoRegistro: " + mensagem);
        return new ResponseEntity(mensagem, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrdemDoseInvalidaException.class)
    protected ResponseEntity handleException(OrdemDoseInvalidaException e) {
        Mensagem mensagem = new Mensagem(e.getMessage());
        LOGGER.info("Tratamentação de exceção OrdemDoseInvalidaException: " + mensagem);
        return new ResponseEntity(mensagem, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IntervaloInsuficienteException.class)
    protected ResponseEntity handleException(IntervaloInsuficienteException e) {
        Mensagem mensagem = new Mensagem(e.getMessage());
        LOGGER.info("Tratamentação de exceção IntervaloInsuficienteException: " + mensagem);
        return new ResponseEntity(mensagem, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApagarException.class)
    protected ResponseEntity handleException(ApagarException e) {
        Mensagem mensagem = new Mensagem(e.getMessage());
        LOGGER.info("Tratamentação de exceção ApagarException: " + mensagem);
        return new ResponseEntity(mensagem, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EditarException.class)
    protected ResponseEntity handleException(EditarException e) {
        Mensagem mensagem = new Mensagem(e.getMessage());
        LOGGER.info("Tratamentação de exceção EditarException: " + mensagem);
        return new ResponseEntity(mensagem, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataInvalidaException.class)
    protected ResponseEntity handleException(DataInvalidaException e) {
        Mensagem mensagem = new Mensagem(e.getMessage());
        LOGGER.info("Tratamentação de exceção DataInvalidaException: " + mensagem);
        return new ResponseEntity(mensagem, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VacinaIncompativelException.class)
    protected ResponseEntity handleException(VacinaIncompativelException e) {
        Mensagem mensagem = new Mensagem(e.getMessage());
        LOGGER.info("Tratamentação de exceção VacinaIncompativelException: " + mensagem);
        return new ResponseEntity(mensagem, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DoseMaiorException.class)
    protected ResponseEntity handleException(DoseMaiorException e) {
        Mensagem mensagem = new Mensagem(e.getMessage());
        LOGGER.info("Tratamentação de exceção DoseMaiorException: " + mensagem);
        return new ResponseEntity(mensagem, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MaximoDoseException.class)
    protected ResponseEntity handleException(MaximoDoseException e) {
        Mensagem mensagem = new Mensagem(e.getMessage());
        LOGGER.info("Tratamentação de exceção DoseMaiorException: " + mensagem);
        return new ResponseEntity(mensagem, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ChaveInvalidaException.class)
    protected ResponseEntity handleException(ChaveInvalidaException e) {
        Mensagem mensagem = new Mensagem(e.getMessage());
        LOGGER.info("Tratamentação de exceção ChaveInvalidaException: " + mensagem);
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
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        String errorMessage = "Ocorreu um erro na aplicação. Nossa equipe de TI já foi notificada e em breve nossos serviços estarão reestabelecidos. Para maiores informações entre em contato em 4002-8922. Lamentamos o ocorrido!";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Mensagem(errorMessage));
    }
}
