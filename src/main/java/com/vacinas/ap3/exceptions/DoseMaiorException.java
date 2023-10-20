package com.vacinas.ap3.exceptions;

import java.io.Serial;

public class DoseMaiorException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public DoseMaiorException(String mensagem) {
        super(mensagem);
    }
}
