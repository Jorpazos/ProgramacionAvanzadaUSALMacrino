package edu.usal.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Singleton: unico punto de acceso a la conexion. Los datos salen de application.properties. */
public class ConexionBD {

    private static ConexionBD instancia;

    private ConexionBD() {
        try {
            Class.forName(PropertiesUtil.get("db.driver"));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("No se encontro el driver JDBC", e);
        }
    }

    public static synchronized ConexionBD getInstance() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    /** Cada llamada devuelve una conexion nueva; quien la pide es responsable de cerrarla. */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                PropertiesUtil.get("db.url"),
                PropertiesUtil.get("db.user"),
                PropertiesUtil.get("db.password"));
    }
}
