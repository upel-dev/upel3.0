package upeldev.com.github.upel3.controllers.new_controllers;

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
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class NewUserController {
    private final UserService userService;

    @Autowired
    public NewUserController(UserService userService) {
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
            @RequestParam(value = "lastName") String lastName,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "index") String index,
            @RequestParam(value = "role", defaultValue = "0") String role,
            Model model,
            Principal principal) {

        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);

        try {
            if (!currentUser.getRoles().contains(Role.ADMIN)) {
                String errorMsg = "Nie masz wystarczających uprawnień, aby rejestrować użytkownika";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }

            String password = generateRandomPassword();
            User newUser = new User(firstName, lastName, email, password);

            newUser.setIndexNumber(index);

            switch (role) {
                case "0":
                    throw new IllegalArgumentException();
                case "1":
                    newUser.getRoles().add(Role.STUDENT);
                    model.addAttribute("new_user_role", "Student"); // To be removed when email service is implemented
                    break;
                case "2":
                    newUser.getRoles().add(Role.LECTURER);
                    model.addAttribute("new_user_role", "Prowadzący"); // To be removed when email service is implemented
                    break;
                case "3":
                    newUser.getRoles().add(Role.ADMIN);
                    model.addAttribute("new_user_role", "Administrator"); // To be removed when email service is implemented
                    break;

            }
            userService.registerNewUser(newUser);

            model.addAttribute("new_user_first_name", firstName); // To be removed when email service is implemented
            model.addAttribute("new_user_last_name", lastName); // To be removed when email service is implemented
            model.addAttribute("new_user_unhashed_password", password); // To be removed when email service is implemented
            model.addAttribute("new_user_email", email); // To be removed when email service is implemented
            model.addAttribute("new_user_index", index); // To be removed when email service is implemented

        } catch (IllegalArgumentException e) {
            String errorMsg = "Podano nieprawidłowe argumenty podczas rejetrowania użytkonika.";
            model.addAttribute("errorMsg", errorMsg);
            return "error";
        }
        return "/new_user_added"; // To be removed when email service is implemented
        // return "redirect:/index";
    }

    private static String generateRandomPassword(){
        final int length = 10;
        final int randNumStart = 48;
        final int randNumEnd = 122;

        SecureRandom random = new SecureRandom();
        return random.ints(randNumStart, randNumEnd)
                .filter(i -> Character.isAlphabetic(i) || Character.isDigit(i))
                .limit(length)
                .mapToObj(i -> String.valueOf((char)i))
                .collect(Collectors.joining());
    }

}

