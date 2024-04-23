package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector {

    private final String URL ="jdbc:mysql://localhost:3306/tunvista";
    private  final String USER="root";
    private  final String PWD="";

    private Connection connection;
    //3 variable pour stocker linstance
    private static MySQLConnector instance ;

    // 1 rendre le constructeur prive
    private MySQLConnector (){
        try {
            connection = DriverManager.getConnection(URL, USER, PWD);
            System.out.println("coonected to DB");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }}

    //2 creer une methode static por utiliser le constructeur   avec une seule instance : sangleton
    public  static MySQLConnector getInstance(){
        if (instance==null){
            instance = new MySQLConnector();
        }return instance;


    }

    public Connection getConnection() {
        return connection;
    }
}