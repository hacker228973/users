package com.hillel.crud.controller;

import com.hillel.crud.model.User;
import com.hillel.crud.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;


@Controller
public class UserController {


    ////////////////////////////////////////////////////////
    @GetMapping("/registration-user")
    public String createRegistrationForm(User user) {
        return "registration-user";
    }

    @PostMapping("/registration")
    public String registrationUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "registration-user";
        }
        userRepository.save(user);
        return "/";
    }
    UserRepository userRepository;
    public UserController(UserRepository userRepository){
        this.userRepository=userRepository;
    }
}
