package com.vacinas.ap3.exceptions;

import java.io.Serial;

public class EditarException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public EditarException(String mensagem) {
        super(mensagem);
    }
}
