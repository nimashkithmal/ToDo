package org.todo.todorails.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.todo.todorails.model.Task;
import org.todo.todorails.model.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByUser(User user);
    
    List<Task> findByUserAndDueDateAndCompleted(User user, LocalDate dueDate, boolean completed);
    
    List<Task> findByUserAndCompleted(User user, boolean completed);
    
    Task findByUserAndId(User user, Long id);
    
    int countByCompleted(boolean completed);
}
