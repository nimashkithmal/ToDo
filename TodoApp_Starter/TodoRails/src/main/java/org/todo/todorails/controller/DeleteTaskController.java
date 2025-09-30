package org.todo.todorails.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.todo.todorails.service.TaskService;

@Controller
@RequestMapping("/task/delete")
public class DeleteTaskController {
    
    @Autowired
    private TaskService taskService;
    
    @PostMapping
    public String deleteTask(@RequestParam Long taskId) {
        boolean deleted = taskService.deleteTask(taskId);
        if (deleted) {
            return "redirect:/dashboard?success=task_deleted";
        } else {
            return "redirect:/dashboard?error=task_not_found";
        }
    }
}
