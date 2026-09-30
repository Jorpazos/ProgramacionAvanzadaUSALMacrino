package ar.edu.usal.logistica.dao.jdbc;

import ar.edu.usal.logistica.dao.DistanciaDAO;
import ar.edu.usal.logistica.excepcion.DAOException;
import ar.edu.usal.logistica.excepcion.ValidacionException;
import ar.edu.usal.logistica.modelo.Destino;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumMap;
import java.util.Map;

/** Implementacion JDBC de DistanciaDAO. */
class DistanciaDAOJdbc implements DistanciaDAO {

    @Override
    public int obtenerKm(Destino origen, Destino destino) throws DAOException, ValidacionException {
        if (origen == null || destino == null || origen == destino) {
            throw new ValidacionException("Debe elegir un origen y un destino distintos.");
        }
        try (Connection con = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement("SELECT km FROM distancia WHERE origen = ? AND destino = ?")) {
            ps.setString(1, origen.name());
            ps.setString(2, destino.name());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("km");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("No se pudo consultar la distancia.", e);
        }
        throw new ValidacionException("No hay distancia cargada entre " + origen.getNombre()
                + " y " + destino.getNombre() + ".");
    }

    @Override
    public Map<Destino, Map<Destino, Integer>> obtenerTabla() throws DAOException {
        Map<Destino, Map<Destino, Integer>> tabla = new EnumMap<>(Destino.class);
        for (Destino d : Destino.values()) {
            tabla.put(d, new EnumMap<>(Destino.class));
        }
        try (Connection con = ConexionDB.getInstance().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT origen, destino, km FROM distancia")) {
            while (rs.next()) {
                tabla.get(Destino.valueOf(rs.getString("origen")))
                        .put(Destino.valueOf(rs.getString("destino")), rs.getInt("km"));
            }
        } catch (SQLException e) {
            throw new DAOException("No se pudo leer la tabla de distancias.", e);
        }
        return tabla;
    }
}
