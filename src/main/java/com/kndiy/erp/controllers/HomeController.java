package com.kndiy.erp.controllers;

import com.kndiy.erp.dto.UserRoleDTO;
import com.kndiy.erp.services.UserAuthorityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;

@Controller
public class HomeController {

    @Autowired
    private UserAuthorityService userAuthorityService;

    @GetMapping("/")
    public String welcome(Principal principal, Model model) {
        model.addAttribute("userRoleDto", new UserRoleDTO());
        model.addAttribute("adminPass", "852456!!!!");
        model.addAttribute("allRoles", userAuthorityService.getAllRoles());
        return  principal == null? "home/login" : "home/welcome";   //logical name of a view,
                            // spring will find a view named: "welcome.html" to response to the request at "/"
    }

    @GetMapping("/error?continue")
    public String welcomeButWithError(Principal principal, Model model) {
        model.addAttribute("userRoleDto", new UserRoleDTO());
        model.addAttribute("adminPass", "852456!!!!");
        model.addAttribute("allRoles", userAuthorityService.getAllRoles());
        return  principal == null? "home/login" : "home/welcome";
    }
    @PostMapping("/register")
    public String registerNewUser(@Valid @ModelAttribute("userRoleDto") UserRoleDTO userRoleDto, Errors errors, RedirectAttributes redirectAttributes) {

        if (!errors.hasErrors()) {
            String result = userAuthorityService.saveUser(userRoleDto);
            redirectAttributes.addFlashAttribute("registerResult", result);
        }
        else {
            ArrayList<String> registerErrors = userAuthorityService.getRegisterUserErrors(errors);
            redirectAttributes.addFlashAttribute("registerErrors", registerErrors);
        }

        return "redirect:/";
    }
}
