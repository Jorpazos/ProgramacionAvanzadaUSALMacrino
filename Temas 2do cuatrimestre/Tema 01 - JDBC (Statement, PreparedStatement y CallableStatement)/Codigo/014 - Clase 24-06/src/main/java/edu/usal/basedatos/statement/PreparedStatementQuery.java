package edu.usal.basedatos.statement;

import edu.usal.basedatos.connection.MySqlConector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PreparedStatementQuery {

    public void actualizar(double promedio, String nombre) throws SQLException {
        String strSQL = "UPDATE estudiantes SET promedio = ? WHERE nombre = ?";
        Connection connection = MySqlConector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(strSQL);

        preparedStatement.setDouble(1, promedio);
        preparedStatement.setString(2, nombre);

        int filasAfectadas = preparedStatement.executeUpdate();

        if(filasAfectadas >= 1){
            System.out.println("Estudiante actualizado con exito");
        }

        preparedStatement.close();
        connection.close();
    }
}
