package ar.edu.usal.logistica.dao;

import ar.edu.usal.logistica.excepcion.DAOException;
import ar.edu.usal.logistica.excepcion.ValidacionException;
import ar.edu.usal.logistica.modelo.Destino;

import java.util.Map;

/** Contrato de acceso a la tabla de distancias entre ciudades. */
public interface DistanciaDAO {

    /** @return kilometros entre ambas ciudades */
    int obtenerKm(Destino origen, Destino destino) throws DAOException, ValidacionException;

    /** Tabla completa origen -> (destino -> km), para mostrarla en pantalla. */
    Map<Destino, Map<Destino, Integer>> obtenerTabla() throws DAOException;
}
