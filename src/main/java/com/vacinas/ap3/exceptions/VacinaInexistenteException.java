package com.vacinas.ap3.exceptions;

import java.io.Serial;

public class VacinaInexistenteException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public VacinaInexistenteException(String mensagem) {
        super(mensagem);
    }
}
