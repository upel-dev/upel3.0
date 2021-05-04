package upeldev.com.github.upel3.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import upeldev.com.github.upel3.auth.Upel3UserDetails;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.services.*;

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
    private final SubActivityService subActivityService;
    private final SubGradeService subGradeService;

    @Autowired
    public ActivityController(ActivityService activityService, UserService userService, GradeService gradeService, SubActivityService subActivityService, SubGradeService subGradeService) {
        this.activityService = activityService;
        this.userService = userService;
        this.gradeService = gradeService;
        this.subActivityService = subActivityService;
        this.subGradeService = subGradeService;
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

            int passValue = activity.getPassValue();
            double maxPoints = activity.getMaxPoints();
            double points = 0;
            List<Grade> grades;
            if(currentUser.getRoles().contains(Role.STUDENT)){
                grades = gradeService.findGradeByCourseUserActivity(activity.getCourse(), currentUser, activity);
                if(!grades.isEmpty()) points = grades.get(grades.size() - 1).getValue();
            }
            else{
                grades = gradeService.findGradeByActivity(activity);
            }

            model.addAttribute("grades", grades);

            Course course = activity.getCourse();
            model.addAttribute("course", course);

            if(currentUser.getCoursesLectured().contains(course) || currentUser.getRoles().contains(Role.ADMIN)) return "activity_lecturer";

            // Progress bar

            String status;
            if(points == 0){
                status = "Brak przydzielonych punktów";
            }
            else if(points < passValue){
                model.addAttribute("barColor", "bg-danger");
                status = "Niezaliczone";
            }
            else{
                model.addAttribute("barColor", "bg-success");
                status = "Zaliczone";
            }
            model.addAttribute("status", status);

            int basePercentage;
            int bonusPercentage = 0;

            if(points < maxPoints){
                basePercentage = (int)(100.0 * points / maxPoints);
            }
            else{
                basePercentage = (int)(100.0 * maxPoints / points);
                bonusPercentage = 100 - basePercentage;
            }
            model.addAttribute("percentage", (int)(100.0 * points / maxPoints));
            model.addAttribute("basePercentage", basePercentage);
            model.addAttribute("bonusPercentage", bonusPercentage);

            if(currentUser.getCoursesEnrolledIn().contains(course)) return "activity_student";

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
