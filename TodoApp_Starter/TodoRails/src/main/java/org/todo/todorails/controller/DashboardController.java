package org.todo.todorails.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.todo.todorails.model.Task;
import org.todo.todorails.model.User;
import org.todo.todorails.service.TaskService;
import org.todo.todorails.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String dashboard(Model model, @RequestParam(required = false) String success) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            User user = userService.findByUsername(username).orElse(null);
            if (user == null) {
                return "redirect:/login?error=true";
            }
            
            // Get all pending tasks for the user (for priority columns)
            List<Task> pendingTasks = taskService.getPendingTasks(user);
            
            // Get all tasks for the user
            List<Task> allTasks = taskService.getAllTasks(user);
            
            // Calculate statistics
            long totalTasks = allTasks.size();
            long completedTasks = allTasks.stream().mapToLong(task -> task.isCompleted() ? 1 : 0).sum();
            long pendingTasksCount = totalTasks - completedTasks;
            
            // Add data to model
            model.addAttribute("user", user);
            model.addAttribute("todaysTasks", pendingTasks != null ? pendingTasks : new ArrayList<>()); // Use pending tasks for priority columns
            model.addAttribute("allTasks", allTasks != null ? allTasks : new ArrayList<>());
            model.addAttribute("totalTasks", totalTasks);
            model.addAttribute("completedTasks", completedTasks);
            model.addAttribute("pendingTasks", pendingTasksCount);
            model.addAttribute("serverTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            model.addAttribute("errorMessage", null);
            
            // Handle success messages
            if ("password_changed".equals(success)) {
                model.addAttribute("successMessage", "Password changed successfully!");
            } else if ("task_added".equals(success)) {
                model.addAttribute("successMessage", "Task added successfully!");
            } else if ("task_updated".equals(success)) {
                model.addAttribute("successMessage", "Task updated successfully!");
            } else if ("task_deleted".equals(success)) {
                model.addAttribute("successMessage", "Task deleted successfully!");
            } else {
                model.addAttribute("successMessage", null);
            }
            
            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading dashboard: " + e.getMessage());
            return "dashboard";
        }
    }
}
