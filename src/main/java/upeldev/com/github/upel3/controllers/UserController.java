package upeldev.com.github.upel3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import upeldev.com.github.upel3.auth.Upel3UserDetails;
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

    private User findCurrentUser(Authentication authentication){
        String currentUserEmail = ((Upel3UserDetails) authentication.getPrincipal()).getUsername();
        return userService.findByEmail(currentUserEmail);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody List<User> findAllUsers(){
        return userService.findAll();
    }

    @RequestMapping(value="/new", method = RequestMethod.POST)
    public @ResponseBody User register(@RequestBody User userDto){
        return userService.registerNewUser(userDto);
    }

    @RequestMapping(value="/{email}", method = RequestMethod.GET)
    public @ResponseBody User findByUsername(@PathVariable("email") String email, Authentication authentication){
        User currentUser = findCurrentUser(authentication);
        if (currentUser.getEmail().equals(email) || userService.isAdmin(currentUser)) {
            User queryUser = userService.findByEmail(email);
            if (queryUser != null)
                return queryUser;
            else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        else
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access is forbidden");
    }

    @RequestMapping(value="/id/{id}", method = RequestMethod.GET)
    public @ResponseBody User findById(@PathVariable("id") Long id, Authentication authentication){
        User currentUser = findCurrentUser(authentication);
        if (currentUser.getId().equals(id) || userService.isAdmin(currentUser)) {
            User queryUser = userService.findById(id);
            if (queryUser != null)
                return queryUser;
            else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        else
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access is forbidden");
    }
}
