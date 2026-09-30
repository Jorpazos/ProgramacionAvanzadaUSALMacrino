package ar.edu.usal.logistica.excepcion;

/**
 * Excepcion base (checked) de la aplicacion. Obliga a quien llama a decidir
 * que hacer ante un error de negocio o de acceso a datos.
 */
public class LogisticaException extends Exception {

    private static final long serialVersionUID = 1L;

    public LogisticaException(String mensaje) {
        super(mensaje);
    }

    public LogisticaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
