package com.vacinas.ap3.exceptions;

import java.io.Serial;

public class ApagarException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ApagarException(String mensagem) {
        super(mensagem);
    }
}
