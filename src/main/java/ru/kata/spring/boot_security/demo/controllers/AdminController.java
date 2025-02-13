package ru.kata.spring.boot_security.demo.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUserHomePage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", userService.findByUsername(user.getUsername()));
        return "admin/adminHome";
    }

    @GetMapping("/allUsers")
    public String showAllUsersPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/allUsers";
    }

    @GetMapping("/add")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/addUser";
    }

    @GetMapping("/{id}/edit")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id).get();

        model.addAttribute("user", user);
        return "admin/editUser";
    }

    @GetMapping("/{id}/remove")
    public String deleteUser(@PathVariable(value = "id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin/allUsers";
    }

    @PostMapping("/add")
    public String saveUser(@ModelAttribute("userToAdd") User user,
                           @RequestParam("role") String[] roleNames) {
        userService.saveUser(user, roleNames);
        return "redirect:/admin/allUsers";
    }

    @PostMapping("/{id}/edit")
    public String updateSelectedUser(@ModelAttribute("user") User user,
                                     @RequestParam("role") String[] roleNames)
    {
        userService.updateUser(user, roleNames);
        return "redirect:/admin/allUsers";
    }
}
