package com.mycompany.tipakeharkka;

import java.sql.*;
/*
/**
 *
 * @author riikoro
 */
public class Main {

    public static void main(String[] args) throws ClassNotFoundException{
        Connection c = createConnection();
        Kayttoliittyma.launch(c);
    }
    
    public static Connection createConnection() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } 
         return connection;
    }
}