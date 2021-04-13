package upeldev.com.github.upel3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.Role;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.CourseService;
import upeldev.com.github.upel3.services.UserService;

import java.security.Principal;
import java.util.List;

@Controller
public class NewUserController {
    private final UserService userService;

    @Autowired
    public NewUserController(UserService userService){
        this.userService = userService;
    }

    @RequestMapping(value = "/new_user")
    public String newUser(Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);
        return "new_user";
    }

    @RequestMapping(value = "/create_user")
    public String createUser(
            @RequestParam(value = "firstName") String firstName,
            @RequestParam(value = "lastName")  String lastName,
            @RequestParam(value = "email")  String email,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "index") String index,
            @RequestParam(value = "role") String role,
            Model model,
            Principal principal) {

        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);

        try{
            if(!currentUser.getRoles().contains(Role.ADMIN)){
                String errorMsg = "Nie masz wystarczających uprawnień, aby rejestrować użytkownika";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }

            User newUser = new User(firstName, lastName, email, password);
            newUser.setIndexNumber(index);
            switch(role)
            {
                case "1":
                    newUser.getRoles().add(Role.STUDENT);
                    break;
                case "2":
                    newUser.getRoles().add(Role.LECTURER);
                    break;
                case "3":
                    newUser.getRoles().add(Role.ADMIN);
                    break;

            }
            userService.registerNewUser(newUser);

        }
        catch (IllegalArgumentException e){
            String errorMsg = "Podano nieprawidłowe argumenty podczas rejetrowania użytkonika.";
            model.addAttribute("errorMsg", errorMsg);
            return "error";
        }
        return "redirect:/index";
    }
}
