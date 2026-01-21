package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

@Controller
@RequestMapping
public class AdminController {

    private final RoleServiceImpl roleServiceImpl;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl, RoleServiceImpl roleServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
    }


    @GetMapping("admin")
    public String printUsers(Model model) {
        model.addAttribute("users", userServiceImpl.getAllUsers());
        return "allUsers";
    }

    @GetMapping("admin/add")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleServiceImpl.findAll());
        return "addUser";
    }

    @PostMapping("admin/add")
    public String addUser(@ModelAttribute("user") User user) {
        userServiceImpl.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("admin/delete")
    public String deleteUser(@ModelAttribute("user") User user) {
        return "deleteUser";
    }

    @DeleteMapping("admin/delete{id}")
    public String deleteUser(@ModelAttribute("user") User user, Long id) {
        userServiceImpl.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("admin/edit")
    public String changeUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleServiceImpl.findAll());
        return "editUser";
    }

    @PatchMapping("admin/edit{id}")
    public String changeUser(@ModelAttribute("user") User user, Long id) {
        userServiceImpl.changeUser(id, user.getFirstName(), user.getLastName(), user.getAge(),
                user.getEmail(), user.getPassword(), user.getRoles());
        return "redirect:/admin";
    }
}

