package edu.usal.basedatos;

import edu.usal.basedatos.statement.PreparedStatementQuery;
import edu.usal.basedatos.statement.StatementQuery;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Pruebas {

    public static void main(String[] args) throws SQLException {
        StatementQuery statementQuery = new StatementQuery();
        //statementQuery.addEstudiante("Estudiante nuevo", 22, "Arquitectura", 6.8);
        //statementQuery.buscarPorNombre("Estudiante nuevo");

        statementQuery.sqlI("pepito", "OR '1' = '1");

        PreparedStatementQuery psQuery = new PreparedStatementQuery();
        //psQuery.actualizar(8.9, "Estudiante nuevo");
    }
}
