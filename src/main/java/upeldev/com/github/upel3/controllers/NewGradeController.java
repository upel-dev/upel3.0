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
import java.util.List;

@Controller
public class NewGradeController {
    private final UserService userService;
    private final CourseService courseService;
    private final ActivityService activityService;
    private final GradeService gradeService;

    @Autowired
    public NewGradeController(UserService userService, CourseService courseService, ActivityService activityService, GradeService gradeService){
        this.userService = userService;
        this.courseService = courseService;
        this.activityService = activityService;
        this.gradeService = gradeService;
    }

    @RequestMapping(value = "/new_grade")
    public String newCourse(@RequestParam(value = "courseId")  Long courseId,
                            @RequestParam(value = "activityId")  Long activityId,
                            Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);
        Course currentCourse = courseService.findCourseById(courseId);
        model.addAttribute("course", currentCourse);

        Activity currentActivity = activityService.findActivityById(activityId);
        model.addAttribute("activity", currentActivity);

        return "new_grade";
    }
    @RequestMapping(value = "/create_grade/{courseId}/{activityId}")
    public String createCourse(
            @PathVariable("courseId") Long courseId,
            @PathVariable("activityId") Long activityId,
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "gradeValue")  int gradeValue,
            Model model,
            Principal principal) {


        User currentUser = userService.findByEmail(principal.getName());

        User modifiedUser = userService.findByEmail(userName);

        Course currentCourse = courseService.findCourseById(courseId);
        model.addAttribute("user", currentUser);

        Activity currentActivity = activityService.findActivityById(activityId);

        try{
            if(!currentUser.getRoles().contains(Role.ADMIN) && !currentCourse.getLecturers().contains(currentUser)){
                String errorMsg = "Nie masz wystarczających uprawnień, aby utworzyć kurs";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }
            Grade newGrade = new Grade(modifiedUser, currentActivity, gradeValue);
            gradeService.save(newGrade);

            List<Course> courses = currentUser.getCoursesLectured();
            model.addAttribute("courses", courses);

        }
        catch (IllegalArgumentException e){
            String errorMsg = "Podano nieprawidłowe argumenty podczas tworzenia kursu.";
            model.addAttribute("errorMsg", errorMsg);
            return "error";
        }

        return "redirect:/activity?id="+activityId;

    }


}
