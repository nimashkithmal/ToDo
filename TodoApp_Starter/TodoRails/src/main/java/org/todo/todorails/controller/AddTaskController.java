package org.todo.todorails.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.todo.todorails.model.Task;
import org.todo.todorails.model.User;
import org.todo.todorails.service.TaskService;
import org.todo.todorails.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/addtask")
public class AddTaskController {
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String showAddTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "addtask";
    }
    
    @PostMapping
    public String processAddTask(
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String shortDescription,
            @RequestParam(required = false) String dueDate,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String type,
            Model model) {
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            User user = userService.findByUsername(username).orElse(null);
            if (user == null) {
                model.addAttribute("errorMessage", "User not found. Please login again.");
                return "addtask";
            }
            
            // Create new task
            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description != null ? description : "");
            task.setShortDescription(shortDescription != null ? shortDescription : "");
            task.setPriority(priority != null ? priority.toUpperCase() : "MEDIUM");
            task.setType(type != null ? type : "");
            task.setDateAdded(LocalDateTime.now());
            task.setUser(user);
            
            // Parse due date if provided
            if (dueDate != null && !dueDate.isEmpty()) {
                try {
                    LocalDate parsedDate = LocalDate.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE);
                    task.setDueDate(parsedDate);
                } catch (Exception e) {
                    model.addAttribute("errorMessage", "Invalid date format. Please use YYYY-MM-DD format.");
                    return "addtask";
                }
            }
            
            // Save the task
            taskService.saveTask(task);
            
            return "redirect:/dashboard?success=task_added";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error adding task: " + e.getMessage());
            return "addtask";
        }
    }
}
