package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexionSQL {


    private static final String user = "SYSTEM";
    private static final String pass = "lolkingjra";
    private static String url = "jdbc:oracle:thin:@localhost:1521:XE";

   

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    public boolean estadoConexion(){
        try(Connection connection = conectar()) {
            return connection.isValid(5);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
   
    
}
