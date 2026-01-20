package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
public class InitData {

    private final UserServiceImpl userService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public InitData(UserServiceImpl userService,
                    RoleRepository roleRepository,
                    UserRepository userRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        if (roleRepository.count() == 0) {
            Role admin = new Role("ROLE_ADMIN");
            Role user = new Role("ROLE_USER");

            roleRepository.save(admin);
            roleRepository.save(user);
        }

        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findAll().stream()
                    .filter(r -> r.getName().equals("ROLE_ADMIN"))
                    .findFirst().get();

            Role userRole = roleRepository.findAll().stream()
                    .filter(r -> r.getName().equals("ROLE_USER"))
                    .findFirst().get();

            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("Adminov");
            admin.setAge("35");
            admin.setEmail("admin@mail.com");
            admin.setPassword("admin");
            admin.setRoles(Set.of(adminRole));

            User user = new User();
            user.setFirstName("User");
            user.setLastName("Userov");
            user.setAge("25");
            user.setEmail("user@mail.com");
            user.setPassword("user");
            user.setRoles(Set.of(userRole));

            userService.addUser(admin);
            userService.addUser(user);
        }
    }
}