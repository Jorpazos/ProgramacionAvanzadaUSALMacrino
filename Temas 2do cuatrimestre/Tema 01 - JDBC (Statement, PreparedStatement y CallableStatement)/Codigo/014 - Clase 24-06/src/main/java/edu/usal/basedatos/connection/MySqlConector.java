package edu.usal.basedatos.connection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConector {

   public static Connection getConnection(){
       try {
           Class.forName("com.mysql.cj.jdbc.Driver");
       } catch (ClassNotFoundException e) {
           System.out.println(e.getMessage());
       }

       String urlJdbc = "jdbc:mysql://localhost:3306/universidad";
       String user = "root";
       String password = "123456";
       Connection connection = null;

       try {
           connection = DriverManager.getConnection(urlJdbc,
                   user,
                   password);


       } catch (SQLException e) {
           System.out.println(e.getMessage());
       }

       return connection;
   }

}
