package com.kndiy.erp.dto;

import com.kndiy.erp.validators.UserRoleDtoConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@UserRoleDtoConstraint
public class UserRoleDto {
    @NotBlank
    @Size(min = 3, message = "Username should have 3 or more characters!")
    private String username;
    @NotBlank
    @Size(min = 8, message = "Password should have 8 or more characters!")
    private String password;
    @NotBlank
    private String reEnterPassword;
    @NotBlank
    @Email (message = "This is not an email!")
    private String email;
    private boolean replaceUser = false;

    private List<String> roles;
}
