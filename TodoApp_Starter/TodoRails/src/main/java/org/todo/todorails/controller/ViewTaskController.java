package org.todo.todorails.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.todo.todorails.model.Task;
import org.todo.todorails.model.User;
import org.todo.todorails.service.TaskService;
import org.todo.todorails.service.UserService;

@Controller
@RequestMapping("/task/viewtask")
public class ViewTaskController {
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public String viewTask(@RequestParam Long taskId, Model model) {
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
                model.addAttribute("errorMessage", "Task not found or you don't have permission to view it.");
                return "redirect:/dashboard";
            }
            
            model.addAttribute("task", task);
            return "viewtask";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error loading task: " + e.getMessage());
            return "redirect:/dashboard";
        }
    }
}

@Controller
@RequestMapping("/task/markDone")
class MarkTaskDoneController {
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public String markTaskDone(@RequestParam Long taskId, Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            User user = userService.findByUsername(username).orElse(null);
            if (user == null) {
                model.addAttribute("errorMessage", "User not found. Please login again.");
                return "redirect:/login";
            }
            
            // Mark task as done
            boolean success = taskService.markTaskAsDone(taskId);
            if (success) {
                return "redirect:/dashboard?success=task_completed";
            } else {
                model.addAttribute("errorMessage", "Task not found or you don't have permission to modify it.");
                return "redirect:/dashboard";
            }
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error marking task as done: " + e.getMessage());
            return "redirect:/dashboard";
        }
    }
}
