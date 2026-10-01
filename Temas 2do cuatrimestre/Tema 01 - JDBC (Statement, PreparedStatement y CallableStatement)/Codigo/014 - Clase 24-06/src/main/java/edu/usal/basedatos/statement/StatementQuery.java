package edu.usal.basedatos.statement;

import edu.usal.basedatos.connection.MySqlConector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StatementQuery {

    public void addEstudiante(String nombre,
                              int edad,
                              String carrera,
                              double promedio) throws SQLException {

        String strSQL = "INSERT INTO estudiantes (nombre, edad, carrera, promedio)\n" +
                "VALUES ('" + nombre + "', " + edad + ", '" + carrera + "', " + promedio + ")";

        Connection connection = MySqlConector.getConnection();
        Statement statement = connection.createStatement();

        int filasAfectadas = statement.executeUpdate(strSQL);

        if(filasAfectadas >= 1) {
            System.out.println("Estudiante dado de alta");
        }

        statement.close();
        connection.close();
    }

    public void buscarPorNombre(String nombre) throws SQLException {
        String strSql = "SELECT * FROM estudiantes WHERE nombre = '" + nombre + "'";

        Connection connection = MySqlConector.getConnection();
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(strSql);

        while(resultSet.next()){
            int id = resultSet.getInt(1);
            String nombreBd = resultSet.getString("nombre");
            String carrera = resultSet.getString("carrera");
            double promedio = resultSet.getDouble("promedio");

            System.out.println("Datos del estudiante : " +
                    "ID : " + id + " Nombre : " + nombre + " Cerrera : " + carrera
            + " promedio : " + promedio);
        }

        statement.close();
        connection.close();
    }

    public void sqlI(String user, String pass){
        String strSQL = "SELECT * FROM usuarios WHERE username = '"
                + user + "' AND password = '" + pass + "'";

        System.out.println(strSQL);
    }
}
