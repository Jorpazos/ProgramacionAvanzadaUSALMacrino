package ar.edu.usal.logistica.dao;

import ar.edu.usal.logistica.excepcion.DAOException;
import ar.edu.usal.logistica.excepcion.ValidacionException;
import ar.edu.usal.logistica.modelo.Viaje;

import java.util.List;

/** Contrato de acceso a datos de viajes. */
public interface ViajeDAO {

    /** Guarda el viaje verificando en la misma transaccion que el camion siga libre. */
    void insertar(Viaje viaje) throws DAOException, ValidacionException;

    List<Viaje> listarTodos() throws DAOException;

    List<Viaje> listarPorChofer(long choferId) throws DAOException;

    /** El chofer marca que inicia un viaje ASIGNADO. */
    void iniciar(long viajeId, long choferId) throws DAOException, ValidacionException;

    /** El chofer marca como realizado un viaje EN_CURSO. */
    void finalizar(long viajeId, long choferId) throws DAOException, ValidacionException;
}
