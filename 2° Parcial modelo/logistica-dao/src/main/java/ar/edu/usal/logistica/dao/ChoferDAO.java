package ar.edu.usal.logistica.dao;

import ar.edu.usal.logistica.excepcion.ChoferNoEncontradoException;
import ar.edu.usal.logistica.excepcion.DAOException;
import ar.edu.usal.logistica.excepcion.ValidacionException;
import ar.edu.usal.logistica.modelo.Chofer;

import java.util.List;

/** Contrato de acceso a datos de choferes (y de su usuario de acceso al sistema). */
public interface ChoferDAO {

    /** Da de alta el chofer, sus camiones autorizados y su usuario, todo en una transaccion. */
    void insertar(Chofer chofer, String passwordInicial) throws DAOException, ValidacionException;

    /** Modifica el chofer y reemplaza sus camiones autorizados, en una transaccion. */
    void actualizar(Chofer chofer) throws DAOException, ValidacionException;

    /** Elimina el chofer, su usuario y sus autorizaciones (falla si tiene viajes). */
    void eliminar(long id) throws DAOException, ValidacionException;

    Chofer buscarPorId(long id) throws DAOException;

    /** @throws ChoferNoEncontradoException si no existe un chofer con ese DNI */
    Chofer buscarPorDni(String dni) throws DAOException, ChoferNoEncontradoException;

    List<Chofer> listarTodos() throws DAOException;
}
