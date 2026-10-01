package edu.usal.basedatos.callable;

import edu.usal.basedatos.connection.MySqlConector;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class CallableSt {

    public String verificarBeca(int id, double montoSolicitado) throws SQLException {
        Connection connection = MySqlConector.getConnection();

        String strSP = "{ call sp_procesar_beca(?, ?, ?)}";

        //Instanciamos el callable
        CallableStatement callableStatement = connection.prepareCall(strSP);

        //Seteo de los comodines
        callableStatement.setInt(1, id);
        callableStatement.setDouble(2, montoSolicitado);

        //3 PARAM del SP - OUT
        callableStatement.registerOutParameter(3, Types.VARCHAR);

        //Exec del SP
        callableStatement.execute();

        //Obtener resultado del SP
        String resultado = callableStatement.getString(3);

        callableStatement.close();
        connection.close();

        return resultado;
    }
}
