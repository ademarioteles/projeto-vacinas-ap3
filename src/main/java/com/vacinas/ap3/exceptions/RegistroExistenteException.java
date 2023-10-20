package com.vacinas.ap3.exceptions;

import java.io.Serial;

public class RegistroExistenteException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public RegistroExistenteException(String mensagem) {
        super(mensagem);
    }
}
