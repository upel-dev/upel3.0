package upeldev.com.github.upel3.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import upeldev.com.github.upel3.auth.Upel3UserDetails;
import upeldev.com.github.upel3.model.Activity;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.ActivityService;
import upeldev.com.github.upel3.services.UserService;

import java.util.List;

@Controller
@RequestMapping(path="/activity")
public class ActivityController {
    private final ActivityService activityService;
    private final UserService userService;


    @Autowired
    public ActivityController(ActivityService activityService, UserService userService) {
        this.activityService = activityService;
        this.userService = userService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody
    List<Activity> getAllActivity(){
        return activityService.findAll();
    }

    @PostMapping("/add")
    public @ResponseBody void postBody(@RequestBody Activity activity, Authentication authentication) {
        String currentUserEmail = ((Upel3UserDetails) authentication.getPrincipal()).getUsername();
        User user = userService.findByEmail(currentUserEmail);

        if(activityService.canUserAddActivity(activity, user)){
            activityService.save(activity);
        }


    }
}
