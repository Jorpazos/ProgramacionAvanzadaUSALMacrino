package ar.edu.usal.logistica.excepcion;

/**
 * Se lanza cuando se busca un chofer por DNI y no existe.
 * El enunciado pide informarlo en pantalla, por eso tiene su propio tipo.
 */
public class ChoferNoEncontradoException extends LogisticaException {

    private static final long serialVersionUID = 1L;

    public ChoferNoEncontradoException(String dni) {
        super("No existe ningún chofer con DNI " + dni + ".");
    }
}
