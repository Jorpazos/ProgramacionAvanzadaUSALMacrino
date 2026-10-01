package edu.usal.basedatos.dao;

import edu.usal.basedatos.domain.Cuenta;
import edu.usal.basedatos.exception.NegocioException;
import edu.usal.basedatos.util.MySqlConector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CuentaDAOImpl implements CuentaDAO{

    private Connection connection;
    private PreparedStatement pStatement;

    @Override
    public void descontarSaldo(Connection connection, Cuenta cuenta) throws SQLException {
        String strSQL = "UPDATE cuentas set saldo = saldo - ? WHERE numero_cuenta = ?";
        this.connection = connection;
        this.pStatement = this.connection.prepareStatement(strSQL);
        this.pStatement.setDouble(1, cuenta.getSaldo());
        this.pStatement.setInt(2, cuenta.getNumeroCuenta());
        int filasAfectadas = this.pStatement.executeUpdate();
        //TODO: validar filas afectadas
    }

    @Override
    public void incrementarSaldo(Connection connection, Cuenta cuenta) throws SQLException {
        String strSQL = "UPDATE cuentas set saldo = saldo + ? WHERE numero_cuenta = ?";
        this.connection = connection;
        this.pStatement = this.connection.prepareStatement(strSQL);
        this.pStatement.setDouble(1, cuenta.getSaldo());
        this.pStatement.setInt(2, cuenta.getNumeroCuenta());
        int filasAfectadas = this.pStatement.executeUpdate();
        //TODO: validar filas afectadas
    }

    @Override
    public double validarSaldo(int numeroCuenta) throws NegocioException {
        String strSQL = "SELECT saldo FROM cuentas WHERE numero_cuenta = ?";
        this.connection = MySqlConector.getConnection();
        try {
            this.pStatement = this.connection.prepareStatement(strSQL);
            this.pStatement.setInt(1, numeroCuenta);
            ResultSet resultSet = this.pStatement.executeQuery();
            return resultSet.getDouble("saldo");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
