package com.vacinas.ap3.exceptions;

import java.io.Serial;

public class DataInvalidaException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public DataInvalidaException(String mensagem) {
        super(mensagem);
    }
}
