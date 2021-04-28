package upeldev.com.github.upel3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.UserService;

import java.security.Principal;

@Controller
public class ProfileController {

    private final UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path="/profile")
    public String newUser(Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);
        model.addAttribute("userService", userService);
        return "profile";
    }




}
