package com.vacinas.ap3.exceptions;

import java.io.Serial;

public class ExteriorException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ExteriorException(String mensagem) {
        super(mensagem);
    }
}
