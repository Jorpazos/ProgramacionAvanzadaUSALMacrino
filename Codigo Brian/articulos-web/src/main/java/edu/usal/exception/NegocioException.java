package edu.usal.exception;

/** Error de regla de negocio (validaciones). El mensaje es apto para mostrar al usuario. */
public class NegocioException extends Exception {

    public NegocioException(String message) {
        super(message);
    }
}
