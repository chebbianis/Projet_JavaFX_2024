package services;

import models.User;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<User> {

    private Connection connection;

    public UserService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public int add(User user) throws SQLException {
        String sql = "insert into user (name,age) VALUES ('"+user.getName() +"',"+ user.getAge()+")";
        System.out.println(sql);
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        return 0;
    }

    @Override
    public void modifier(User user) throws SQLException {

    }

    @Override
    public void supprimer(int id) throws SQLException {

    }

    @Override
    public List<User> recuperer() throws SQLException {
        return List.of();
    }


}
