package com.kndiy.erp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDTO {
    @NotBlank
    @Size(min = 3, message = "Username should have 3 or more characters!")
    private String username;
    @NotBlank
    @Size(min = 8, message = "Password should have 8 or more characters!")
    private String password;
    @NotBlank
    @Email (message = "This is not an email!")
    private String email;
    @NotEmpty(message = "Please select at least one Role!")
    private List<String> roles;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
