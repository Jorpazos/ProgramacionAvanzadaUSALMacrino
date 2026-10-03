package edu.usal.dao.interfaces;

import edu.usal.domain.Articulo;
import edu.usal.domain.ResumenInventario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ArticuloDAO {

    /** PreparedStatement - INSERT con parametros del usuario. */
    void crear(Articulo articulo, Connection con) throws SQLException;

    /** PreparedStatement - SELECT parametrizado. */
    boolean existePorCodigo(String codigo, Connection con) throws SQLException;

    /** Statement - SELECT estatico, sin parametros. */
    List<Articulo> listarTodos(Connection con) throws SQLException;

    /** CallableStatement - Stored Procedure con parametros OUT. */
    ResumenInventario obtenerResumen(Connection con) throws SQLException;
}
