package com.kndiy.erp.validators;

import com.kndiy.erp.dto.UserRoleDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserRoleDtoValidator implements ConstraintValidator<UserRoleDtoConstraint, UserRoleDto> {
    @Override
    public void initialize(UserRoleDtoConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserRoleDto userRoleDTO, ConstraintValidatorContext constraintValidatorContext) {

        constraintValidatorContext.disableDefaultConstraintViolation();

        String pass = userRoleDTO.getPassword();
        String rePass = userRoleDTO.getReEnterPassword();

        if (!pass.equals(rePass)) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "Passwords do not match!"
            ).addConstraintViolation();
            return false;
        }

        if (userRoleDTO.getRoles().isEmpty()) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "Please assign at least one role for each user!"
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
