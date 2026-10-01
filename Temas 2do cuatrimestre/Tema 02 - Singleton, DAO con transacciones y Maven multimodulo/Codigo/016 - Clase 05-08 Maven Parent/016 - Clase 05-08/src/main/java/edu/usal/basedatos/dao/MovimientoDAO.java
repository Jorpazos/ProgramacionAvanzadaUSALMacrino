package edu.usal.basedatos.dao;

import edu.usal.basedatos.domain.Moviento;

import java.sql.Connection;

public interface MovimientoDAO {

    void registrarMovimiento(Connection connection, Moviento moviento);
}
