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
    public List<Role> listRoles() {
        return roleService.findAll();
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

    @Override
    public boolean changeUser(User user) {
        User dbUser = userRepository.findById(user.getId()).orElse(null);
        if (dbUser == null) return false;

        dbUser.setFirstName(user.getFirstName());
        dbUser.setLastName(user.getLastName());
        dbUser.setAge(user.getAge());
        dbUser.setEmail(user.getEmail());
        dbUser.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = user.getRoles().stream()
                .map(r -> roleService.findById(r.getId()))
                .collect(Collectors.toSet());
        dbUser.setRoles(roles);

        return true;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }
}