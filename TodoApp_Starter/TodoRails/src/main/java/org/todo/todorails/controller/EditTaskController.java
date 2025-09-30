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
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/task/edittask")
public class EditTaskController {
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String showEditTaskForm(@RequestParam Long id, Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            User user = userService.findByUsername(username).orElse(null);
            if (user == null) {
                model.addAttribute("errorMessage", "User not found. Please login again.");
                return "redirect:/login";
            }
            
            // Find the task by ID
            Task task = taskService.findTaskById(id);
            if (task == null) {
                model.addAttribute("errorMessage", "Task not found or you don't have permission to edit it.");
                return "redirect:/dashboard";
            }
            
            model.addAttribute("task", task);
            return "edittask";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error loading task: " + e.getMessage());
            return "redirect:/dashboard";
        }
    }
    
    @PostMapping
    public String showEditTaskFormPost(@RequestParam Long taskId, Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            User user = userService.findByUsername(username).orElse(null);
            if (user == null) {
                model.addAttribute("errorMessage", "User not found. Please login again.");
                return "redirect:/login";
            }
            
            // Find the task by ID
            Task task = taskService.findTaskById(taskId);
            if (task == null) {
                model.addAttribute("errorMessage", "Task not found or you don't have permission to edit it.");
                return "redirect:/dashboard";
            }
            
            model.addAttribute("task", task);
            return "edittask";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error loading task: " + e.getMessage());
            return "redirect:/dashboard";
        }
    }
}

@Controller
@RequestMapping("/task/updatetask")
class UpdateTaskController {
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public String processUpdateTask(
            @RequestParam Long id,
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
                return "redirect:/login";
            }
            
            // Find the existing task
            Task existingTask = taskService.findTaskById(id);
            if (existingTask == null) {
                model.addAttribute("errorMessage", "Task not found or you don't have permission to edit it.");
                return "redirect:/dashboard";
            }
            
            // Update task properties
            existingTask.setTitle(title);
            existingTask.setDescription(description != null ? description : "");
            existingTask.setShortDescription(shortDescription != null ? shortDescription : "");
            existingTask.setPriority(priority != null ? priority.toUpperCase() : "MEDIUM");
            existingTask.setType(type != null ? type : "");
            
            // Parse due date if provided
            if (dueDate != null && !dueDate.isEmpty()) {
                try {
                    LocalDate parsedDate = LocalDate.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE);
                    existingTask.setDueDate(parsedDate);
                } catch (Exception e) {
                    model.addAttribute("errorMessage", "Invalid date format. Please use YYYY-MM-DD format.");
                    model.addAttribute("task", existingTask);
                    return "edittask";
                }
            } else {
                existingTask.setDueDate(null);
            }
            
            // Save the updated task
            Task updatedTask = taskService.updateTask(existingTask);
            if (updatedTask == null) {
                model.addAttribute("errorMessage", "Error updating task. Please try again.");
                model.addAttribute("task", existingTask);
                return "edittask";
            }
            
            return "redirect:/dashboard?success=task_updated";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating task: " + e.getMessage());
            return "redirect:/dashboard";
        }
    }
}
