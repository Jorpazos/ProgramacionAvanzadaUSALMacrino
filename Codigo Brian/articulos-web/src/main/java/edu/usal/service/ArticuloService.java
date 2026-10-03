package edu.usal.service;

import edu.usal.dao.factory.ArticuloDAOFactory;
import edu.usal.dao.interfaces.ArticuloDAO;
import edu.usal.domain.Articulo;
import edu.usal.domain.ResumenInventario;
import edu.usal.exception.NegocioException;
import edu.usal.util.ConexionBD;
import edu.usal.util.PropertiesUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/** Capa de servicio: valida reglas de negocio y maneja la transaccion JDBC. */
public class ArticuloService {

    private final ArticuloDAO articuloDAO = ArticuloDAOFactory.getArticuloDAO(PropertiesUtil.get("dao.origen"));

    public List<Articulo> listar() throws SQLException {
        try (Connection con = ConexionBD.getInstance().getConnection()) {
            return articuloDAO.listarTodos(con);
        }
    }

    public ResumenInventario obtenerResumen() throws SQLException {
        try (Connection con = ConexionBD.getInstance().getConnection()) {
            return articuloDAO.obtenerResumen(con);
        }
    }

    public void crear(Articulo a) throws NegocioException, SQLException {
        validar(a);

        try (Connection con = ConexionBD.getInstance().getConnection()) {
            con.setAutoCommit(false);
            try {
                if (articuloDAO.existePorCodigo(a.getCodigo(), con)) {
                    throw new NegocioException("Ya existe un artículo con el código " + a.getCodigo());
                }
                articuloDAO.crear(a, con);
                con.commit();
            } catch (SQLException | NegocioException e) {
                con.rollback();
                throw e;
            }
        }
    }

    private void validar(Articulo a) throws NegocioException {
        if (a.getCodigo() == null || a.getCodigo().trim().isEmpty()) {
            throw new NegocioException("El código es obligatorio.");
        }
        if (a.getNombre() == null || a.getNombre().trim().isEmpty()) {
            throw new NegocioException("El nombre es obligatorio.");
        }
        if (a.getPrecio() < 0) {
            throw new NegocioException("El precio no puede ser negativo.");
        }
        if (a.getStock() < 0) {
            throw new NegocioException("El stock no puede ser negativo.");
        }

        double precioMax = PropertiesUtil.getDouble("articulo.precio.max", Double.MAX_VALUE);
        int stockMax = PropertiesUtil.getInt("articulo.stock.max", Integer.MAX_VALUE);
        if (a.getPrecio() > precioMax) {
            throw new NegocioException("El precio no puede superar " + precioMax + ".");
        }
        if (a.getStock() > stockMax) {
            throw new NegocioException("El stock no puede superar " + stockMax + ".");
        }
        a.setCodigo(a.getCodigo().trim());
        a.setNombre(a.getNombre().trim());
        if (a.getDescripcion() != null) {
            a.setDescripcion(a.getDescripcion().trim());
        }
    }
}
