package org.todo.todorails.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.todo.todorails.model.User;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserService userService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(new ArrayList<>()) // No specific authorities for now
                .build();
    }
}
