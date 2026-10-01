package ar.edu.usal.logistica.dao;

import ar.edu.usal.logistica.excepcion.DAOException;
import ar.edu.usal.logistica.excepcion.ValidacionException;
import ar.edu.usal.logistica.modelo.Camion;

import java.util.List;

/** Contrato de acceso a datos de camiones. */
public interface CamionDAO {

    void insertar(Camion camion) throws DAOException, ValidacionException;

    void actualizar(Camion camion) throws DAOException, ValidacionException;

    void eliminar(long id) throws DAOException, ValidacionException;

    Camion buscarPorId(long id) throws DAOException;

    List<Camion> listarTodos() throws DAOException;

    /** Camiones que el chofer puede manejar y que no estan en viaje. */
    List<Camion> listarDisponiblesParaChofer(long choferId) throws DAOException;
}
