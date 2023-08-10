package com.kndiy.erp.services;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;

@Service
public class ErrorHandlingService {

    public List<String> parseError(Errors errors) {
        List<String> res = new ArrayList<>();

        errors.getAllErrors().forEach(err -> {
            res.add(err.getDefaultMessage());
        });
        return res;
    }
}
