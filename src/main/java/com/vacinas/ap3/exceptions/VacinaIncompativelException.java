package com.vacinas.ap3.exceptions;

import java.io.Serial;

public class VacinaIncompativelException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public VacinaIncompativelException(String mensagem) {
        super(mensagem);
    }
}
