package ar.edu.usal.logistica.dao.jdbc;

import ar.edu.usal.logistica.excepcion.DAOException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * PATRON SINGLETON: existe una unica instancia que lee la configuracion una
 * sola vez y entrega conexiones nuevas a quien las pide. Cada DAO abre su
 * conexion, la usa y la cierra (try-with-resources).
 */
public final class ConexionDB {

    // Inicializacion perezosa y segura entre hilos (holder idiom)
    private static final class Holder {
        private static final ConexionDB INSTANCIA = new ConexionDB();
    }

    private final String url;
    private final String usuario;
    private final String clave;

    private ConexionDB() {
        Properties props = new Properties();
        try (InputStream in = ConexionDB.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException("No se encontró db.properties en el classpath");
            }
            props.load(in);
            Class.forName(props.getProperty("db.driver"));
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("No se pudo inicializar la conexión a la base de datos", e);
        }
        // Las variables de entorno tienen prioridad (util para produccion sin tocar el codigo)
        this.url = valor("DB_URL", props.getProperty("db.url"));
        this.usuario = valor("DB_USER", props.getProperty("db.user"));
        this.clave = valor("DB_PASSWORD", props.getProperty("db.password"));
    }

    public static ConexionDB getInstance() {
        return Holder.INSTANCIA;
    }

    /** Abre una conexion nueva. Quien la pide es responsable de cerrarla. */
    public Connection getConexion() throws DAOException {
        try {
            return DriverManager.getConnection(url, usuario, clave);
        } catch (SQLException e) {
            throw new DAOException("No se pudo conectar con la base de datos.", e);
        }
    }

    private static String valor(String variableEntorno, String porDefecto) {
        String env = System.getenv(variableEntorno);
        return env != null && !env.isEmpty() ? env : porDefecto;
    }
}
