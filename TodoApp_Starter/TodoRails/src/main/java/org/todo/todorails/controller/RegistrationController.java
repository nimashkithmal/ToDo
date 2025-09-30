package org.todo.todorails.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.todo.todorails.model.User;
import org.todo.todorails.service.UserService;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
    @PostMapping
    public String processRegistration(User user, Model model) {
        try {
            // Check if username already exists
            if (userService.existsByUsername(user.getUsername())) {
                model.addAttribute("errorMessage", "Username already exists. Please choose a different username.");
                return "register";
            }
            
            // Check if email already exists
            if (userService.existsByEmail(user.getEmail())) {
                model.addAttribute("errorMessage", "Email already exists. Please use a different email.");
                return "register";
            }
            
            // Hash the password
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            
            // Save the user
            userService.saveUser(user);
            
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registration failed. Please try again.");
            return "register";
        }
    }
}
