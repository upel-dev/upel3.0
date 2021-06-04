package upeldev.com.github.upel3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.services.ActivityService;
import upeldev.com.github.upel3.services.CourseService;
import upeldev.com.github.upel3.services.GradeService;
import upeldev.com.github.upel3.services.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class GradeSummaryController {
    private final CourseService courseService;
    private final UserService userService;
    private final GradeService gradeService;
    private final ActivityService activityService;
    @Autowired
    GradeSummaryController(CourseService courseService, UserService userService, GradeService gradeService, ActivityService activityService){
        this.courseService = courseService;
        this.userService = userService;
        this.gradeService = gradeService;
        this.activityService = activityService;
    }
    @RequestMapping(value = "/grade_summary")
    public String gradeSummary(@RequestParam(value = "id")  Long courseId, Model model, Principal principal){
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);
        Course currentCourse = courseService.findCourseById(courseId);
        model.addAttribute("course", currentCourse);

        List<Grade> gradeList = activityService.findActivityByCourse(currentCourse).
                stream().
                map(activity -> activityService.getStudentGradeInActivity(activity, currentUser)).
                filter(Objects::nonNull).
                collect(Collectors.toList());

        double userValue = courseService.getUserValueInCourse(currentCourse, currentUser);
        double valueMAX = currentCourse.getValue();

        int basePercentage;
        int bonusPercentage = 0;

        if(userValue < valueMAX){
            basePercentage = (int)(100.0 * userValue / valueMAX);
        }
        else{
            basePercentage = (int)(100.0 * valueMAX / userValue);
            bonusPercentage = 100 - basePercentage;
        }

        model.addAttribute("gradeList", gradeList);
        model.addAttribute("userValue", userValue);
        model.addAttribute("valueMAX", valueMAX);

        model.addAttribute("percentage", (int)(100.0 * userValue / valueMAX));
        model.addAttribute("basePercentage", basePercentage);
        model.addAttribute("bonusPercentage", bonusPercentage);

        String status;
        if(userValue < currentCourse.getPassValue()){
            model.addAttribute("barColor", "bg-danger");
            status = "Niezaliczony";
        }
        else{
            model.addAttribute("barColor", "bg-success");
            status = "Zaliczony";
        }
        for(Grade grade : gradeList){
            if(grade.getValue() <= grade.getActivity().getPassValue()){
                status = "Niezaliczony";
            }
        }
        model.addAttribute("status", status);

        return "grade_summary";
    }


}
