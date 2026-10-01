package edu.usal.basedatos.dao;

import edu.usal.basedatos.domain.Cuenta;
import edu.usal.basedatos.exception.NegocioException;

import java.sql.Connection;
import java.sql.SQLException;

public interface CuentaDAO {

    //Connection no se notifica en el contrato del metodo o accion

    void descontarSaldo(Connection connection, Cuenta cuenta) throws SQLException;
    void incrementarSaldo(Connection connection, Cuenta cuenta) throws SQLException;
    double validarSaldo(int numeroCuenta) throws NegocioException;

}
