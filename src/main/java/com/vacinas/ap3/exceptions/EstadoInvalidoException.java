package com.vacinas.ap3.exceptions;

import java.io.Serial;

public class EstadoInvalidoException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public EstadoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
