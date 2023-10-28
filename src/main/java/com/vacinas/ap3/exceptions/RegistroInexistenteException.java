package com.vacinas.ap3.exceptions;

import java.io.Serial;

public class RegistroInexistenteException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public RegistroInexistenteException(String mensagem) {
        super(mensagem);
    }
}
