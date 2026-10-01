package ar.edu.usal.logistica.excepcion;

/**
 * Error de regla de negocio o de datos ingresados por el usuario
 * (DNI duplicado, camion ocupado, categoria insuficiente, etc.).
 * Su mensaje es apto para mostrarse en pantalla.
 */
public class ValidacionException extends LogisticaException {

    private static final long serialVersionUID = 1L;

    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
