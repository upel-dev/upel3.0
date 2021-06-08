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
import java.util.*;
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

            Course course = activity.getCourse();
            model.addAttribute("course", course);

            double passValue = activity.getPassValue();
            double maxPoints = activity.getValue();
            double points = 0;
            boolean gradeIsSet = false;

            if(currentUser.getCoursesEnrolledIn().contains(course)){
                Grade grade = activityService.getStudentGradeInActivity(activity, currentUser);
                if(grade != null){
                    points = grade.getValue();
                    gradeIsSet = true;
                }


                model.addAttribute("grade", grade);
            }
            else{
                List<Grade> grades = gradeService.findGradeByActivity(activity);
                model.addAttribute("grades", grades);
            }

            if(currentUser.getCoursesLectured().contains(course) || currentUser.getRoles().contains(Role.ADMIN)) {
                List<User> ungradedStudents = course.
                        getEnrolledStudents().
                        stream().
                        filter(user -> activityService.getStudentGradeInActivity(activity, user) == null).
                        collect(Collectors.toList());
                model.addAttribute("ungradedStudents", ungradedStudents);
                return "activity_lecturer";
            }

            // Progress bar
            String status;

            if(!gradeIsSet){
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

            if(currentUser.getCoursesEnrolledIn().contains(course)){
                // Leaderboard spot

                Map<Double, String> userGradeMap = new HashMap<Double, String>();
                Set<User> users = activity.getCourse().getEnrolledStudents();
                double currentUserValue = 0;

                for (User user : users) {
                    Grade userGrade = activityService.getStudentGradeInActivity(activity, user);
                    double userValue = userGrade.getValue();
                    userGradeMap.put(userValue, user.getEmail());
                    if(currentUser == user)
                        currentUserValue = userValue;
                }

                NavigableMap<Double, String> sortedUserGradeMap = new TreeMap<Double, String>(userGradeMap).descendingMap();

                int leaderboardNumber = 0;
                leaderboardNumber = sortedUserGradeMap.headMap(currentUserValue).size() + 1;
                model.addAttribute("number", leaderboardNumber);

                return "activity_student";
            }

            if(!currentUser.getRoles().contains(Role.ADMIN)){
                model.addAttribute("errorMsg", errorMsg);
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

    @RequestMapping(value = "/delete_grade/{courseId}/{activityId}/{gradeId}", method = RequestMethod.GET)
    public String deleteGrade(@PathVariable("courseId") Long courseId,
                                    @PathVariable("activityId") Long activityId,
                                    @PathVariable("gradeId") Long gradeId,
                                    Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());

        model.addAttribute("user", currentUser);

        Activity currentActivity = activityService.findActivityById(activityId);
        Grade currentGrade = gradeService.findGradeById(gradeId);
        activityService.removeGrade(currentActivity, currentGrade);

        return "redirect:/activity?id=" + activityId;
    }
}
