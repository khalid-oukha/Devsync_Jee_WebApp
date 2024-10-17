package org.example.entities;

import jakarta.persistence.*;
import lombok.Builder;

import java.util.List;

@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "is_manager", nullable = false)
    private Boolean isManager;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Task> tasks;

    @Column(name = "delete_token", nullable = false)
    private int deleteToken;

    @Column(name = "update_token", nullable = false)
    private int updateToken;

    public User() {
        this.updateToken = 2;
        this.deleteToken = 1;
    }

    public User(String username, String password, String firstName, String lastName, String email, Boolean isManager) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isManager = isManager;
        this.deleteToken = 1;
    }

    public User(Long id, String username, String password, String firstName, String lastName, String email, Boolean isManager) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isManager = isManager;
        this.deleteToken = 1;
    }

    public User(Long id, String username, String password, String firstName, String lastName, String email, Boolean isManager, List<Task> tasks, int deleteToken, int updateToken) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isManager = isManager;
        this.tasks = tasks;
        this.deleteToken = deleteToken;
        this.updateToken = updateToken;
    }


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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsManager() {
        return isManager;
    }

    public void setIsManager(Boolean isManager) {
        this.isManager = isManager;
    }

    public int getDeleteToken() {
        return deleteToken;
    }

    public void setDeleteToken(int deleteToken) {
        this.deleteToken = deleteToken;
    }

    public int getUpdateToken() {
        return updateToken;
    }

    public void setUpdateToken(int updateToken) {
        this.updateToken = updateToken;
    }
}
