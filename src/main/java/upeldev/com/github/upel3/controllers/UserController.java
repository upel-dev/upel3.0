package upeldev.com.github.upel3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.UserService;

import java.util.List;

@Controller
@RequestMapping(path="/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody List<User> getAllUsers(){
        return userService.findAll();
    }

    @RequestMapping(value="/{email}", method = RequestMethod.GET)
    public @ResponseBody User getByUsername(@PathVariable("email") String email){
        return userService.findByEmail(email);
    }
}
