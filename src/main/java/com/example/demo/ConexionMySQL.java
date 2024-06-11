package com.example.demo;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class ConexionMySQL {
    private static final String URL_MYSQL = "jdbc:mysql://localhost:3306/prueba2";
    private static final String USUARIO_MYSQL = "root";
    private static final String CONTRASEÑA_MYSQL = "Comic.07";

    public static Connection conectarMySQL() throws SQLException {
        return DriverManager.getConnection(URL_MYSQL, USUARIO_MYSQL, CONTRASEÑA_MYSQL);
    }
}
