package com.vacinas.ap3.exceptions;

import java.io.Serial;

public class IntervaloInsuficienteException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public IntervaloInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
