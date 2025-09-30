package org.todo.todorails.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.todo.todorails.model.User;
import org.todo.todorails.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public boolean changePassword(String currentPassword, String newPassword, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        
        if (user != null && passwordEncoder.matches(currentPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
