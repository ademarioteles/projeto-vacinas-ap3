package com.vacinas.ap3.exceptions;

import java.io.Serial;

public class ChaveInvalidaException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ChaveInvalidaException(String mensagem) {
        super(mensagem);
    }
}
