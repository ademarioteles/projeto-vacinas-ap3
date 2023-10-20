package com.vacinas.ap3.exceptions;

import java.io.Serial;

public class PacienteInexistenteException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public PacienteInexistenteException(String mensagem) {
        super(mensagem);
    }
}
