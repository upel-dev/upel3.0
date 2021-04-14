package upeldev.com.github.upel3.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import upeldev.com.github.upel3.auth.Upel3UserDetails;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.services.ActivityService;
import upeldev.com.github.upel3.services.GradeService;
import upeldev.com.github.upel3.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path="/activity")
public class ActivityController {
    private final ActivityService activityService;
    private final UserService userService;
    private final GradeService gradeService;

    @Autowired
    public ActivityController(ActivityService activityService, UserService userService, GradeService gradeService) {
        this.activityService = activityService;
        this.userService = userService;
        this.gradeService = gradeService;
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
            List<Grade> grades;
            if(currentUser.getRoles().contains(Role.STUDENT)){
                grades = gradeService.findGradeByUser(currentUser);
                grades = grades
                        .stream()
                        .filter(grade -> grade.getActivity().equals(activity)
                                && grade.getUser().equals(currentUser))
                        .collect(Collectors.toList());
            }
            else{
                grades = gradeService.findAll()
                        .stream()
                        .filter(grade -> grade.getActivity().equals(activity))
                        .collect(Collectors.toList());
            }

            model.addAttribute("grades", grades);

            Course course = activity.getCourse();
            model.addAttribute("course", course);

//            model.addAttribute("grades", activity.getGrade());

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
