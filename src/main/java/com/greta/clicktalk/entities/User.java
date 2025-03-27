package com.greta.clicktalk.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class User {

    private long id;

    @Email(message = "you must write a valid email")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 14, max = 25, message = "The password must be between 14 and 25 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,20}$",
            message = "Password must contain at least one uppercase letter, one digit, and one special character"
    )
    private String password;
    private String role;

    public User() {
    }

    public User(long id, String email, String password, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // getters and setters
    public long getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getRole() {
        return role;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
