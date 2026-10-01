package ar.edu.usal.logistica.dao.jdbc;

import ar.edu.usal.logistica.dao.ChoferDAO;
import ar.edu.usal.logistica.excepcion.ChoferNoEncontradoException;
import ar.edu.usal.logistica.excepcion.DAOException;
import ar.edu.usal.logistica.excepcion.ValidacionException;
import ar.edu.usal.logistica.modelo.Camion;
import ar.edu.usal.logistica.modelo.Chofer;
import ar.edu.usal.logistica.util.PasswordUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Implementacion JDBC de ChoferDAO. Las altas y modificaciones tocan varias tablas: usan transaccion. */
class ChoferDAOJdbc implements ChoferDAO {

    // Un chofer con todos sus camiones autorizados en una sola consulta (LEFT JOIN)
    private static final String SELECT_BASE =
            "SELECT ch.id AS ch_id, ch.nombre AS ch_nombre, ch.apellido AS ch_apellido, ch.dni AS ch_dni, "
                    + "ch.fecha_nacimiento AS ch_fecha_nacimiento, ch.categoria AS ch_categoria, "
                    + "ch.telefono AS ch_telefono, "
                    + "c.id AS c_id, c.marca AS c_marca, c.modelo AS c_modelo, c.dominio AS c_dominio, "
                    + "c.toneladas_max AS c_toneladas_max, c.capacidad_tanque_litros AS c_capacidad_tanque_litros, "
                    + "c.consumo_litros_km AS c_consumo_litros_km "
                    + "FROM chofer ch "
                    + "LEFT JOIN chofer_camion cc ON cc.chofer_id = ch.id "
                    + "LEFT JOIN camion c ON c.id = cc.camion_id ";
    private static final String ORDEN = " ORDER BY ch.apellido, ch.nombre, c.marca, c.modelo";

    private static final String INSERT_CHOFER =
            "INSERT INTO chofer (nombre, apellido, dni, fecha_nacimiento, categoria, telefono) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_CHOFER =
            "UPDATE chofer SET nombre = ?, apellido = ?, dni = ?, fecha_nacimiento = ?, categoria = ?, telefono = ? "
                    + "WHERE id = ?";
    private static final String INSERT_AUTORIZACION = "INSERT INTO chofer_camion (chofer_id, camion_id) VALUES (?, ?)";
    private static final String INSERT_USUARIO =
            "INSERT INTO usuario (username, password_hash, rol, chofer_id) VALUES (?, ?, 'CHOFER', ?)";

    @Override
    public void insertar(Chofer chofer, String passwordInicial) throws DAOException, ValidacionException {
        chofer.validar();
        if (passwordInicial == null || passwordInicial.length() < 6) {
            throw new ValidacionException("La contraseña inicial debe tener al menos 6 caracteres.");
        }
        Connection con = null;
        try {
            con = ConexionDB.getInstance().getConexion();
            con.setAutoCommit(false); // Inicio de la transaccion: o se guarda todo o no se guarda nada

            try (PreparedStatement ps = con.prepareStatement(INSERT_CHOFER, Statement.RETURN_GENERATED_KEYS)) {
                cargarDatos(ps, chofer);
                ps.executeUpdate();
                try (ResultSet claves = ps.getGeneratedKeys()) {
                    claves.next();
                    chofer.setId(claves.getLong(1));
                }
            }
            guardarAutorizaciones(con, chofer);
            // El usuario del chofer se llama igual que su DNI
            try (PreparedStatement ps = con.prepareStatement(INSERT_USUARIO)) {
                ps.setString(1, chofer.getDni());
                ps.setString(2, PasswordUtil.hashear(passwordInicial));
                ps.setLong(3, chofer.getId());
                ps.executeUpdate();
            }
            con.commit();
        } catch (SQLException e) {
            JdbcUtil.rollbackSilencioso(con, e);
            chofer.setId(null);
            throw traducir(e, chofer, "No se pudo guardar el chofer.");
        } finally {
            JdbcUtil.cerrar(con);
        }
    }

    @Override
    public void actualizar(Chofer chofer) throws DAOException, ValidacionException {
        chofer.validar();
        Connection con = null;
        try {
            con = ConexionDB.getInstance().getConexion();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(UPDATE_CHOFER)) {
                cargarDatos(ps, chofer);
                ps.setLong(7, chofer.getId());
                if (ps.executeUpdate() == 0) {
                    throw new ValidacionException("El chofer que intenta modificar no existe.");
                }
            }
            // Se reemplazan las autorizaciones: se borran las viejas y se cargan las nuevas
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM chofer_camion WHERE chofer_id = ?")) {
                ps.setLong(1, chofer.getId());
                ps.executeUpdate();
            }
            guardarAutorizaciones(con, chofer);
            // El usuario sigue el DNI del chofer si este cambio
            try (PreparedStatement ps = con.prepareStatement("UPDATE usuario SET username = ? WHERE chofer_id = ?")) {
                ps.setString(1, chofer.getDni());
                ps.setLong(2, chofer.getId());
                ps.executeUpdate();
            }
            con.commit();
        } catch (SQLException e) {
            JdbcUtil.rollbackSilencioso(con, e);
            throw traducir(e, chofer, "No se pudo modificar el chofer.");
        } catch (ValidacionException e) {
            JdbcUtil.rollbackSilencioso(con, e);
            throw e;
        } finally {
            JdbcUtil.cerrar(con);
        }
    }

    @Override
    public void eliminar(long id) throws DAOException, ValidacionException {
        // Las FK con ON DELETE CASCADE borran usuario, autorizaciones y tokens; si hay viajes la BD lo impide
        try (Connection con = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement("DELETE FROM chofer WHERE id = ?")) {
            ps.setLong(1, id);
            if (ps.executeUpdate() == 0) {
                throw new ValidacionException("El chofer que intenta eliminar no existe.");
            }
        } catch (SQLException e) {
            if (JdbcUtil.esClaveForanea(e)) {
                throw new ValidacionException("No se puede eliminar el chofer porque tiene viajes registrados.");
            }
            throw new DAOException("No se pudo eliminar el chofer.", e);
        }
    }

    @Override
    public Chofer buscarPorId(long id) throws DAOException {
        try (Connection con = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(SELECT_BASE + "WHERE ch.id = ?" + ORDEN)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                List<Chofer> choferes = leer(rs);
                return choferes.isEmpty() ? null : choferes.get(0);
            }
        } catch (SQLException e) {
            throw new DAOException("No se pudo consultar el chofer.", e);
        }
    }

    @Override
    public Chofer buscarPorDni(String dni) throws DAOException, ChoferNoEncontradoException {
        try (Connection con = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(SELECT_BASE + "WHERE ch.dni = ?" + ORDEN)) {
            ps.setString(1, dni == null ? "" : dni.trim());
            try (ResultSet rs = ps.executeQuery()) {
                List<Chofer> choferes = leer(rs);
                if (choferes.isEmpty()) {
                    throw new ChoferNoEncontradoException(dni);
                }
                return choferes.get(0);
            }
        } catch (SQLException e) {
            throw new DAOException("No se pudo consultar el chofer.", e);
        }
    }

    @Override
    public List<Chofer> listarTodos() throws DAOException {
        try (Connection con = ConexionDB.getInstance().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(SELECT_BASE + ORDEN)) {
            return leer(rs);
        } catch (SQLException e) {
            throw new DAOException("No se pudo listar los choferes.", e);
        }
    }

    /** Agrupa las filas del LEFT JOIN: varias filas del mismo chofer = varios camiones autorizados. */
    private List<Chofer> leer(ResultSet rs) throws SQLException {
        Map<Long, Chofer> choferes = new LinkedHashMap<>();
        while (rs.next()) {
            long id = rs.getLong("ch_id");
            Chofer chofer = choferes.get(id);
            if (chofer == null) {
                chofer = Mapeadores.chofer(rs, "ch_");
                choferes.put(id, chofer);
            }
            rs.getLong("c_id");
            if (!rs.wasNull()) { // si el chofer no tiene camiones, las columnas del camion vienen en NULL
                chofer.autorizar(Mapeadores.camion(rs, "c_"));
            }
        }
        return new ArrayList<>(choferes.values());
    }

    private void cargarDatos(PreparedStatement ps, Chofer chofer) throws SQLException {
        ps.setString(1, chofer.getNombre());
        ps.setString(2, chofer.getApellido());
        ps.setString(3, chofer.getDni());
        ps.setDate(4, Date.valueOf(chofer.getFechaNacimiento()));
        ps.setString(5, chofer.getCategoria().name());
        ps.setString(6, chofer.getTelefono());
    }

    private void guardarAutorizaciones(Connection con, Chofer chofer) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(INSERT_AUTORIZACION)) {
            for (Camion camion : chofer.getCamionesAutorizados()) {
                ps.setLong(1, chofer.getId());
                ps.setLong(2, camion.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private DAOException traducir(SQLException e, Chofer chofer, String mensajeGenerico) throws ValidacionException {
        if (JdbcUtil.esDuplicado(e)) {
            throw new ValidacionException("Ya existe un chofer con DNI " + chofer.getDni() + ".");
        }
        if (JdbcUtil.esClaveForanea(e)) {
            throw new ValidacionException("Alguno de los camiones seleccionados ya no existe.");
        }
        return new DAOException(mensajeGenerico, e);
    }
}
