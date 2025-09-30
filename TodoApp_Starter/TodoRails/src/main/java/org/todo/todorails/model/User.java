package org.todo.todorails.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String email;
    
    @Column
    private String highestQualification;
    
    @ElementCollection
    @CollectionTable(name = "user_hobbies", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "hobby")
    private List<String> hobbies = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks;
    
    // Constructors
    public User() {}
    
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public List<Task> getTasks() {
        return tasks;
    }
    
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    
    public String getHighestQualification() {
        return highestQualification;
    }
    
    public void setHighestQualification(String highestQualification) {
        this.highestQualification = highestQualification;
    }
    
    public List<String> getHobbies() {
        return hobbies;
    }
    
    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }
}
