package org.todo.todorails.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.todo.todorails.service.UserService;

@Controller
@RequestMapping("/changepasswd")
public class ChangePasswordController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping
    public String showChangePasswordForm(Model model) {
        return "changepasswd";
    }
    
    @PostMapping
    public String changePassword(@RequestParam String currentPassword, 
                                @RequestParam String newPassword, 
                                @RequestParam String confirmPassword, 
                                Model model) {
        
        // Validate passwords match
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "New passwords do not match.");
            return "changepasswd";
        }
        
        // Validate password length
        if (newPassword.length() < 6) {
            model.addAttribute("errorMessage", "New password must be at least 6 characters long.");
            return "changepasswd";
        }
        
        // Attempt to change password
        boolean success = userService.changePassword(currentPassword, newPassword, passwordEncoder);
        
        if (success) {
            return "redirect:/dashboard?success=password_changed";
        } else {
            model.addAttribute("errorMessage", "Current password is incorrect.");
            return "changepasswd";
        }
    }
}
