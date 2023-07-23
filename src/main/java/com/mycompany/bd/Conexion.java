package com.mycompany.bd;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    /* 
        * ****************************************************
                        AUTOR: JOEL ZARPAN
                METODO: CONEXION A LA BASE DE DATOS
        * ****************************************************
     */
    public static Connection conectar() {

        try {
            String name = Dotenv.load().get("DB_NAME");
            String host = Dotenv.load().get("DB_HOST");
            String username = Dotenv.load().get("DB_USERNAME");
            String password = Dotenv.load().get("DB_PASSWORD");

            String url = "jdbc:mysql://" + host + "/" + name + "?sslMode=VERIFY_IDENTITY";
            Connection cn = DriverManager.getConnection(url, username, password);
            return cn;
        } catch (SQLException e) {
            System.out.println("Error en la conexión local " + e);
        }
        return null;
    }
}
