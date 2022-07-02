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
import java.util.Objects;
import java.util.Optional;


@Controller
public class UserController {
    static String nameAdmin = "Nik";

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
                    System.out.println("Вход выполнен пользователем " + user);
                    return goToOwnPage(user, model);
                }

            }
        }

        System.out.println("Вход не выполнен");
        return "redirect:/";
    }

    ////////////////////////////////////////////////////////
    public String goToOwnPage(User user, Model model) {

        model.addAttribute("user", user);
        return "own-page";
    }

    public String goToAdminPage(User user, Model model) {
        model.addAttribute("users", userRepository.findAll());

        return "admin-page";
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


    @GetMapping("/dataToConsole/{id}")
    public String dataToConsole(@PathVariable("id") int id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        System.out.println(user);
        model.addAttribute("user", user);


        return goToOwnPage(user, model);
    }

    @GetMapping("/admin-page/{id}")
    public String adminPage(@PathVariable("id") int id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        if (user.getName().equals(nameAdmin)) {
            return goToAdminPage(user, model);
        }
        return goToOwnPage(user, model);
    }

    @GetMapping("/edit-user/{id}")
    public String editUserForm(User user) {
        return "edit-user";
    }

    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable("id") int id, User user, Model model) {
        ArrayList<User> users = (ArrayList<User>) userRepository.findAll();
        for (User value : users) {
            if (user.getName().equals(value.getName())) {
                return goToAdminPageWithAdminProfile(model);
            }
        }
        User userById = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        if (userById.getName().equals(nameAdmin)) {
            return goToAdminPageWithAdminProfile(model);
        }
        System.out.println("Изменен пользователь " + userById);
        if (user.getName().equals("Оставить")||user.getName().equals("")) {
            user.setName(userById.getName());
        }
        if (user.getPassword().equals("Оставить")||user.getPassword().equals("")) {
            user.setPassword(userById.getPassword());
        }
        userRepository.save(user);

        System.out.println("На " + user);


        return goToAdminPageWithAdminProfile(model);
    }

    public String goToAdminPageWithAdminProfile(Model model) {
        ArrayList<User> users = (ArrayList<User>) userRepository.findAll();
        for (User value : users) {
            if (value.getName().equals(nameAdmin)) {

                User user = value;

                return goToAdminPage(user, model);
            }


        }
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));


        if (!user.getName().equals(nameAdmin)) {
            userRepository.deleteById(id);
            System.out.println("Удален пользователь " + user);
        }
        ArrayList<User> users = (ArrayList<User>) userRepository.findAll();


        return goToAdminPageWithAdminProfile(model);


    }
}