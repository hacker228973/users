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
import java.util.ArrayList;
import java.util.Optional;


@Controller
public class UserController {


    ////////////////////////////////////////////////////////
    @GetMapping("/registration-user")
    public String createRegistrationForm(User user) {
        return "registration-user";
    }

    @GetMapping("/login-user")
    public String createLoginForm(User user) {
        return "login-user";
    }

    ////////////////////////////////////////////////////////
    @PostMapping("/registration")
    public String registrationUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "registration-user";
        }

        ArrayList<User> users = (ArrayList<User>) userRepository.findAll();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getName().equals(user.getName())) {
                return "registration-user";
            }
        }

        System.out.println(user);
        userRepository.save(user);
        return "redirect:/";
    }

    // // // // // // // // // // // // // // // // // // // //
    @PostMapping("/login")
    public String loginUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "login-user";
        }

        ArrayList<User> users = (ArrayList<User>) userRepository.findAll();
        for (User value : users) {
            if (value.getName().equals(user.getName())) {
                if (value.getPassword().equals(user.getPassword())) {
                    user = value;

                    return goToOwnPage(user,model);
                }

            }
        }

        System.out.println("Вход не выполнен");
        return "redirect:/";
    }
//    @GetMapping("/own-page")
//    public String createOwnPage(User user){
//
//        return "own-page";
//    }
    ////////////////////////////////////////////////////////
    public String goToOwnPage(User user,Model model) {
        System.out.println("Вход выполнен пользователем "+user);
        model.addAttribute("users", user);
        return "own-page";
    }

    @GetMapping
    public String showUserList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    ////////////////////////////////////////////////////////
    @PostMapping("/requestToBD")
    public String completeRequestToBD(@Valid User user) {
        if (user.getName().equals("Nik")) {
            System.out.println("Вход в базу данных произведен");
            return "h2";
        }
        System.out.println("Ошибка");
        return "/";
    }
    @GetMapping("/dataToConsole/{name}")
    public String dataToConsole(@PathVariable("name") String name,BindingResult result,Model model){
        if (result.hasErrors()) {
            return "login-user";
        }
        ArrayList<User> users = (ArrayList<User>) userRepository.findAll();

        for (User value : users) {
            System.out.println(value);
            if (value.getName().equals(name)) {
                    System.out.println("print "+value);
                    return goToOwnPage(value,model);
                }


        }

        return "redirect:/";
    }

}

