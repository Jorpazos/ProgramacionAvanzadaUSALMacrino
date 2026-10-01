package edu.usal.basedatos;

import edu.usal.basedatos.callable.CallableSt;

import java.sql.SQLException;

public class PruebaSP {

    public static void main(String[] args) throws SQLException {
        CallableSt callableSt = new CallableSt();
        String resultado = callableSt.verificarBeca(47, 5000.0);
        System.out.println("RESULTAOD SP : " + resultado);
    }

    /*

        PUNTO 1: Obtener un listado general de todos los libros registrados con su stock.
         - SELECT * FROM libros;
         - Resolucion : PS, executeQuery() [] || ST, executeQuery() [X]

        PUNTO 2: Permitir al usuario buscar libros introduciendo parte del titulo o del autor.
         - SELECT * FROM libros WHERE titulo LIKE '%titulo%' OR autor LIKE '%autor%'
         - Resolución : PS, executeQuery()

        PUNTO 3: Ejecutar el SP para registrar el prestamo de un libro a un usuario y obtener
        el resultado de respuesta.
         - Resolución : Callable, execute()

     */
}
