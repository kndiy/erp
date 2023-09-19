package com.kndiy.erp.services;

import com.kndiy.erp.dto.UserRoleDto;
import com.kndiy.erp.entities.userCluster.Role;
import com.kndiy.erp.entities.userCluster.User;
import com.kndiy.erp.repositories.RoleRepository;
import com.kndiy.erp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAuthorityService  {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public String saveUser(UserRoleDto userRoleDTO) {

        User check = userRepository.findByUsername(userRoleDTO.getUsername());

        if (check != null && !userRoleDTO.isReplaceUser()) {
            return "User: " + userRoleDTO.getUsername() + " already exists! If you want to edit User, please select \"Replace User\"";
        }

        check = new User();
        check.setUsername(userRoleDTO.getUsername());
        check.setPassword(passwordEncoder.encode(userRoleDTO.getPassword()));
        check.setEmail(userRoleDTO.getEmail());
        check.setEnabled(true);

        List<String> roles = userRoleDTO.getRoles();
        List<Role> roleObjectList = new ArrayList<>();

        for (String role : roles) {
            Role roleObject = roleRepository.findByRole(role);
            if (roleObject == null) {
                roleObject = createNewRole(role);
            }
            roleObjectList.add(roleObject);
        }

        check.setRoles(roleObjectList);
        userRepository.save(check);

        return "User: " + check.getUsername() + " was successfully created!";
    }

    public ArrayList<String> getRegisterUserErrors(Errors bindingResult) {
        ArrayList<String> res = new ArrayList<>();

        bindingResult.getAllErrors().forEach(error -> {
            res.add(error.getDefaultMessage());
        });

        return res;
    }

    private Role createNewRole(String role) {
        Role roleObject = new Role();
        roleObject.setRole(role);
        return roleRepository.save(roleObject);
    }

    public List<String> getAllRoles() {
        Role.RoleEnum[] allRoles = Role.RoleEnum.values();
        return Arrays.stream(allRoles).map(Role.RoleEnum::toString).collect(Collectors.toList());
    }
}
