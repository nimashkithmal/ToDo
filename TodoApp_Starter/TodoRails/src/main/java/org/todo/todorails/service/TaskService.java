package org.todo.todorails.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.todo.todorails.model.Task;
import org.todo.todorails.model.User;
import org.todo.todorails.repository.TaskRepository;
import org.todo.todorails.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public Task saveTask(Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            task.setUser(user);
        }
        return taskRepository.save(task);
    }
    
    public List<Task> getTodayTasksForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return taskRepository.findByUserAndDueDateAndCompleted(user, LocalDate.now(), false);
        }
        return List.of();
    }
    
    public boolean markTaskAsDone(Long taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            Task task = taskRepository.findByUserAndId(user, taskId);
            if (task != null) {
                task.setCompleted(true);
                task.setCompletionDate(LocalDateTime.now());
                taskRepository.save(task);
                return true;
            }
        }
        return false;
    }
    
    public int countByCompleted(boolean completed) {
        return taskRepository.countByCompleted(completed);
    }
    
    public List<Task> getTodaysTasks(User user) {
        return taskRepository.findByUserAndDueDateAndCompleted(user, LocalDate.now(), false);
    }
    
    public List<Task> getAllTasks(User user) {
        return taskRepository.findByUser(user);
    }
    
    public List<Task> getPendingTasks(User user) {
        return taskRepository.findByUserAndCompleted(user, false);
    }
    
    public Task findTaskById(Long taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return taskRepository.findByUserAndId(user, taskId);
        }
        return null;
    }
    
    public Task updateTask(Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            task.setUser(user);
            return taskRepository.save(task);
        }
        return null;
    }
    
    public boolean deleteTask(Long taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            Task task = taskRepository.findByUserAndId(user, taskId);
            if (task != null) {
                taskRepository.delete(task);
                return true;
            }
        }
        return false;
    }
}
