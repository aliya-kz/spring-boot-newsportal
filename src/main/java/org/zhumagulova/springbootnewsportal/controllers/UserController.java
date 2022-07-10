package org.zhumagulova.springbootnewsportal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.zhumagulova.springbootnewsportal.models.User;
import org.zhumagulova.springbootnewsportal.service.UserService;

import javax.validation.Valid;

@Slf4j
@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userService.save(user);
        return "login";
    }
/*
    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("user", new User());
        return "security/login";
    }


    @PostMapping("/login")
    public String login(@ModelAttribute @Valid User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "security/login";
        }
        boolean authentificated = userService.userAuthentificated(user.getEmail(), user.getPassword());
        if (authentificated) {
            userService.loadUserByUsername(user.getEmail());
            return "redirect:/admin/index";
        } else {
            return "redirect:/security/login";
        }
    }*/
}
