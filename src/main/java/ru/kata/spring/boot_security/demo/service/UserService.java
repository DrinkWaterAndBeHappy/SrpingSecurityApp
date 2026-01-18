package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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

    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    public boolean addUser(User user) {
        Set<Role> roles = user.getRoles().stream()
                .map(r -> roleRepository.findById(r.getId()).orElse(null))
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

    public boolean changeUser(Long id, String newFirstName, String newLastName, String newAge, String newEmail, String newPassword) {
        if (userRepository.findById(id).isEmpty()) {
            return false;
        }
        userRepository.findById(id).get().setFirstName(newFirstName);
        userRepository.findById(id).get().setLastName(newLastName);
        userRepository.findById(id).get().setAge(newAge);
        userRepository.findById(id).get().setEmail(newEmail);
        userRepository.findById(id).get().setPassword(passwordEncoder.encode(newPassword));
        return true;
    }
}