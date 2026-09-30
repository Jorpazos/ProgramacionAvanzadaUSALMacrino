package ar.edu.usal.logistica.dao.jdbc;

import ar.edu.usal.logistica.modelo.Camion;
import ar.edu.usal.logistica.modelo.Categoria;
import ar.edu.usal.logistica.modelo.Chofer;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Convierte filas de un ResultSet en objetos del dominio. Se centraliza aca
 * para no repetir el mapeo en cada DAO. El prefijo permite leer columnas con
 * alias cuando una consulta une varias tablas (ej: "ch_nombre", "c_marca").
 */
final class Mapeadores {

    private Mapeadores() {
    }

    static Camion camion(ResultSet rs, String prefijo) throws SQLException {
        return new Camion(
                rs.getLong(prefijo + "id"),
                rs.getString(prefijo + "marca"),
                rs.getString(prefijo + "modelo"),
                rs.getString(prefijo + "dominio"),
                rs.getDouble(prefijo + "toneladas_max"),
                rs.getDouble(prefijo + "capacidad_tanque_litros"),
                rs.getDouble(prefijo + "consumo_litros_km"));
    }

    static Chofer chofer(ResultSet rs, String prefijo) throws SQLException {
        return new Chofer(
                rs.getLong(prefijo + "id"),
                rs.getString(prefijo + "nombre"),
                rs.getString(prefijo + "apellido"),
                rs.getString(prefijo + "dni"),
                rs.getDate(prefijo + "fecha_nacimiento").toLocalDate(),
                Categoria.valueOf(rs.getString(prefijo + "categoria")),
                rs.getString(prefijo + "telefono"));
    }
}
