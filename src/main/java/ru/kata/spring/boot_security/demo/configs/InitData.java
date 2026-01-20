package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
public class InitData {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public InitData(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void init() {
        Role adminRole = roleService.createIfNotExists("ROLE_ADMIN");
        Role userRole = roleService.createIfNotExists("ROLE_USER");

        if (!userService.existsByEmail("admin@mail.com")) {
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("Adminov");
            admin.setAge("35");
            admin.setEmail("admin@mail.com");
            admin.setPassword("admin");
            admin.setRoles(Set.of(adminRole));
            userService.addUser(admin);
        }

        if (!userService.existsByEmail("user@mail.com")) {
            User user = new User();
            user.setFirstName("User");
            user.setLastName("Userov");
            user.setAge("25");
            user.setEmail("user@mail.com");
            user.setPassword("user");
            user.setRoles(Set.of(userRole));
            userService.addUser(user);
        }
    }
}