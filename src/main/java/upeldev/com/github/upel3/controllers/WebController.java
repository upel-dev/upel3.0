package upeldev.com.github.upel3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.UserService;

import javax.persistence.Access;
import java.security.Principal;

@Controller
public class WebController {
    private final UserService userService;

    @Autowired
    public WebController(UserService userService){
        this.userService = userService;
    }

    @RequestMapping(value = "/index")
    public String index(Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("firstname", currentUser.getFirstName());
        model.addAttribute("lastname", currentUser.getLastName());
        return "index";
    }

    @RequestMapping(value = "/course")
    public String course(Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("firstname", currentUser.getFirstName());
        model.addAttribute("lastname", currentUser.getLastName());
        return "course";
    }

    @RequestMapping(value = "/new_course")
    public String new_course(Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("firstname", currentUser.getFirstName());
        model.addAttribute("lastname", currentUser.getLastName());
        return "new_course";
    }

    @RequestMapping(value = "/profile")
    public String profile(Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("firstname", currentUser.getFirstName());
        model.addAttribute("lastname", currentUser.getLastName());
        return "profile";
    }
}