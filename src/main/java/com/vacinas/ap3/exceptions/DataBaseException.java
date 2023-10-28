package com.vacinas.ap3.exceptions;

import java.io.Serial;

public class DataBaseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public DataBaseException(String mensagem) {
        super(mensagem);
    }
}
