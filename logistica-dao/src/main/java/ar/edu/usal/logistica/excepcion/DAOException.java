package ar.edu.usal.logistica.excepcion;

/**
 * Error tecnico de acceso a datos (SQLException, driver, conexion, etc.).
 * Envuelve la excepcion original para no filtrar detalles de JDBC a la capa web.
 */
public class DAOException extends LogisticaException {

    private static final long serialVersionUID = 1L;

    public DAOException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public DAOException(String mensaje) {
        super(mensaje);
    }
}
