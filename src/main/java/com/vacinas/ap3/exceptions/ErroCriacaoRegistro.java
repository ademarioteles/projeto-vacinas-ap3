package com.vacinas.ap3.exceptions;

import java.io.Serial;

public class ErroCriacaoRegistro extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ErroCriacaoRegistro(String mensagem) {
        super(mensagem);
    }
}
