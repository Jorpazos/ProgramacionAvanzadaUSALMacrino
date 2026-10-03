package edu.usal.dao.factory;

import edu.usal.dao.impl.ArticuloDAOImpl;
import edu.usal.dao.interfaces.ArticuloDAO;

/**
 * Factory: elige la implementacion del DAO segun el origen de datos.
 * Para sumar otra (ej. "MEM" con ArrayList) solo se agrega un if aca.
 */
public class ArticuloDAOFactory {

    public static ArticuloDAO getArticuloDAO(String origen) {
        if ("JDBC".equalsIgnoreCase(origen)) {
            return new ArticuloDAOImpl();
        }
        throw new RuntimeException("Implementacion inexistente para el origen: " + origen);
    }
}
