package edu.usal.basedatos.services;

import edu.usal.basedatos.dao.CuentaDAO;
import edu.usal.basedatos.dao.CuentaDAOImpl;
import edu.usal.basedatos.dao.MovimientoDAO;
import edu.usal.basedatos.dao.MovimientoDAOImpl;
import edu.usal.basedatos.domain.Cuenta;
import edu.usal.basedatos.domain.Moviento;
import edu.usal.basedatos.exception.NegocioException;
import edu.usal.basedatos.util.MySqlConector;

import java.sql.Connection;
import java.sql.SQLException;

public class TransferenciaService {

    private CuentaDAO  cuentaDAO;
    private MovimientoDAO movimientoDAO;

    public TransferenciaService() {
        this.cuentaDAO = new CuentaDAOImpl();
        this.movimientoDAO = new MovimientoDAOImpl();
    }

    public void transferir(){
        Connection connection = MySqlConector.getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try{

            //1er Paso - Validar saldo cuenta origen
            this.cuentaDAO.validarSaldo(0);
        }catch (NegocioException e){
            //Manejo de exception
        }

        try {

            //2do Paso - DescontarSaldo cuenta origen
            this.cuentaDAO.descontarSaldo(connection, new Cuenta());

            //3er Paso - Incrementar cuenta destino
            this.cuentaDAO.incrementarSaldo(connection, new Cuenta());

            //4to Paso - Registrar moviento
            this.movimientoDAO.registrarMovimiento(connection, new Moviento());

            connection.commit();

        } catch (SQLException e){
            //Manejo de excepcion
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

}
