package com.vacinas.ap3.exceptions;

import java.io.Serial;

public class OrdemDoseInvalidaException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public OrdemDoseInvalidaException(String mensagem) {
        super(mensagem);
    }
}
