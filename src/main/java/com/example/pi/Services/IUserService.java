package com.example.pi.Services;
import com.example.pi.Entities.User;

public interface IUserService {
    User getUserById(int id);
    void saveUser(User user);
    void updateUser(User user);
    void deleteUser(int id);
}
