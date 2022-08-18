package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.model.User;
import ru.otus.services.UserService;

@Controller
public class WebPageController {

    private final UserService userService;

    public WebPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String mainPage(Model model) {
        return "redirect:/index.html";
    }

    @GetMapping("/users")
    public String usersPage(Model model) {
        var randomUser = userService.getRandomUser();
        if (randomUser.isPresent()) {
            model.addAttribute("randomUser", randomUser.get());
        } else {
            model.addAttribute("randomUser", new User(0L, "", "", ""));
        }
        return "users";
    }
}
