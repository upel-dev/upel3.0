package upeldev.com.github.upel3.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import upeldev.com.github.upel3.auth.Upel3UserDetails;
import upeldev.com.github.upel3.model.Activity;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.Role;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.ActivityService;
import upeldev.com.github.upel3.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
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

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public String activity(Model model, Principal principal, HttpServletRequest request){

        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);

        String errorMsg = "Wystąpił błąd";

        try {
            Long id = Long.parseLong(request.getParameter("id").toString());

            Activity activity = activityService.findActivityById(id);

            if(activity == null){
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }

            model.addAttribute("activity", activity);

            Course course = activity.getCourse();
            model.addAttribute("course", course);

            model.addAttribute("grades", activity.getGrade());

            if(currentUser.getCoursesEnrolledIn().contains(course)) return "activity_student";
            if(currentUser.getCoursesLectured().contains(course)) return "activity_lecturer";
            if(!currentUser.getRoles().contains(Role.ADMIN)){
                model.addAttribute(errorMsg);
                return "error";
            }

        }
        catch(NumberFormatException nfe) {
            errorMsg = "Zapytanie GET /activity?id=<activity_id> otrzymało niewłaściwy typ danych. Spodziewany typ: long integer.";
            model.addAttribute("errorMsg", errorMsg);
            return "error";
        }

        return "error";
    }
}
