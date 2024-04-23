package com.example.pi.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MyDataBase {

    private final String url = "jdbc:mysql://localhost:3306/tunvista";
    private final String user = "root";
    private final String pwd = "";
    private Connection connection;
    private static MyDataBase instance;

    public MyDataBase() {
        try {
            connection = DriverManager.getConnection(url, user, pwd);
            System.out.println("Connected");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static MyDataBase getInstance() {
        if(instance == null)
            instance = new MyDataBase();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}