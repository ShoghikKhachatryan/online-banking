package com.example.onlinebankingsystem.controller;

import com.example.onlinebankingsystem.exception.AlreadyUserExistException;
import com.example.onlinebankingsystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showRegistrationForm() {
        return "signup";
    }

    public String signUp(@RequestParam String username,
                         @RequestParam String password,
                         Model model) {
        try {
            userService.saveUser(username, password);
        } catch (AlreadyUserExistException ex) {
            model.addAttribute("message", ex.getMessage());
            return "signup";
        }
        return "redirect:/login?success=true";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("errorMessage", "Invalid Credentials !!");
        return "login"; // Return the login view
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout"; // Redirect to login page with logout parameter
    }
}
