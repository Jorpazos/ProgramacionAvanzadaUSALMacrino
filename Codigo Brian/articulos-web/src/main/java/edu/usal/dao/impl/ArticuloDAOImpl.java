package edu.usal.dao.impl;

import edu.usal.dao.interfaces.ArticuloDAO;
import edu.usal.domain.Articulo;
import edu.usal.domain.ResumenInventario;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion JDBC del DAO. No abre ni cierra conexiones: las recibe del Servicio.
 * Cada metodo muestra un tipo de sentencia distinto.
 */
public class ArticuloDAOImpl implements ArticuloDAO {

    // ------------------------------------------------------------------
    // 1) Statement: SQL estatico, SIN datos del usuario.
    //    Es valido aca porque la consulta es fija. Si se concatenaran
    //    parametros en el String, seria vulnerable a SQL Injection.
    // ------------------------------------------------------------------
    @Override
    public List<Articulo> listarTodos(Connection con) throws SQLException {
        List<Articulo> lista = new ArrayList<>();
        String sql = "SELECT id, codigo, nombre, descripcion, precio, stock "
                   + "FROM articulos ORDER BY id DESC";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Articulo(
                        rs.getInt("id"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")));
            }
        }
        return lista;
    }

    // ------------------------------------------------------------------
    // 2) PreparedStatement: consultas parametrizadas (?).
    //    Seguro contra SQL Injection y pre-compilado. Se usa siempre que
    //    haya datos que vienen del usuario.
    // ------------------------------------------------------------------
    @Override
    public void crear(Articulo a, Connection con) throws SQLException {
        String sql = "INSERT INTO articulos (codigo, nombre, descripcion, precio, stock) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getCodigo());
            ps.setString(2, a.getNombre());
            ps.setString(3, a.getDescripcion());
            ps.setDouble(4, a.getPrecio());
            ps.setInt(5, a.getStock());
            ps.executeUpdate();
        }
    }

    @Override
    public boolean existePorCodigo(String codigo, Connection con) throws SQLException {
        String sql = "SELECT 1 FROM articulos WHERE codigo = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // ------------------------------------------------------------------
    // 3) CallableStatement: ejecuta un Stored Procedure.
    //    Los parametros OUT se registran con registerOutParameter y se
    //    leen despues de execute().
    // ------------------------------------------------------------------
    @Override
    public ResumenInventario obtenerResumen(Connection con) throws SQLException {
        String sql = "{ call sp_resumen_inventario(?, ?, ?) }";

        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter(1, Types.INTEGER);   // p_cantidad
            cs.registerOutParameter(2, Types.INTEGER);   // p_stock_total
            cs.registerOutParameter(3, Types.DECIMAL);   // p_valor_total

            cs.execute();

            return new ResumenInventario(
                    cs.getInt(1),
                    cs.getInt(2),
                    cs.getDouble(3));
        }
    }
}
