package ru.hamz.eventform.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.hamz.eventform.models.ERole;
import ru.hamz.eventform.models.User;
import ru.hamz.eventform.services.UserDetailsServiceImpl;

import java.security.Principal;

@Controller
public class AuthController {

    private final UserDetailsServiceImpl userService;

    @Autowired
    public AuthController(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/authorize")
    public String redirectToOwnPage(Principal principal) {
        User user = userService.findByEmail(principal.getName());

        return user.getRole().equals(ERole.ROLE_ADMIN) ? "redirect:/admin/events/to-work" : "redirect:/tutor/events/new";
    }
}
