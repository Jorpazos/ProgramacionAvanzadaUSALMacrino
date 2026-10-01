package ar.edu.usal.logistica.dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/** Utilidades compartidas por los DAO JDBC (codigos de error y manejo de transacciones). */
final class JdbcUtil {

    /** Codigo de MySQL: valor duplicado en una clave unica. */
    static final int ERR_DUPLICADO = 1062;
    /** Codigos de MySQL: violacion de clave foranea (hijo y padre). */
    static final int ERR_FK_PADRE = 1451;
    static final int ERR_FK_HIJO = 1452;

    private JdbcUtil() {
    }

    static boolean esDuplicado(SQLException e) {
        return e.getErrorCode() == ERR_DUPLICADO;
    }

    static boolean esClaveForanea(SQLException e) {
        return e.getErrorCode() == ERR_FK_PADRE || e.getErrorCode() == ERR_FK_HIJO;
    }

    /** Deshace la transaccion sin ocultar la excepcion original si el rollback tambien falla. */
    static void rollbackSilencioso(Connection con, Throwable causa) {
        if (con == null) {
            return;
        }
        try {
            con.rollback();
        } catch (SQLException ex) {
            causa.addSuppressed(ex);
        }
    }

    /** Devuelve la conexion al modo normal (autocommit) y la cierra. */
    static void cerrar(Connection con) {
        if (con == null) {
            return;
        }
        try {
            con.setAutoCommit(true);
            con.close();
        } catch (SQLException ignorada) {
            // Al cerrar no hay nada mas que hacer; el error original (si hubo) ya se informo
        }
    }
}
