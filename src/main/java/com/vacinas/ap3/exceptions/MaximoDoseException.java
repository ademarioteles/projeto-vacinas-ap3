package com.vacinas.ap3.exceptions;

import java.io.Serial;

public class MaximoDoseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public MaximoDoseException(String mensagem) {
        super(mensagem);
    }
}
