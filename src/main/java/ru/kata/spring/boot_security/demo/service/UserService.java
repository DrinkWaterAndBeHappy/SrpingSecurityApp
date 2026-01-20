package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    List<Role> listRoles();
    boolean addUser(User user);
    boolean deleteUser(Long id);
    boolean changeUser(Long id,
                       String newFirstName,
                       String newLastName,
                       String newAge,
                       String newEmail,
                       String newPassword);
    boolean existsByEmail(String email);
}
