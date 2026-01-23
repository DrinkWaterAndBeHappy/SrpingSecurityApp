package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean addUser(User user) {
        Set<Role> roles = user.getRoles().stream()
                .map(r -> roleService.findById(r.getId()))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    public boolean changeUser(Long id, String newFirstName, String newLastName, String newAge,
                              String newEmail, String newPassword, Set<Role> newRoles) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) return false;

        User user = optionalUser.get();
        user.setFirstName(newFirstName);
        user.setLastName(newLastName);
        user.setAge(newAge);
        user.setEmail(newEmail);

        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        Set<Role> roles = newRoles.stream()
                .map(r -> roleService.findById(r.getId()))
                .collect(Collectors.toSet());
        user.setRoles(roles);

        return true;
    }


}