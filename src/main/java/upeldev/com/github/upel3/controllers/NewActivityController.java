package upeldev.com.github.upel3.controllers;

import com.sun.xml.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.services.ActivityService;
import upeldev.com.github.upel3.services.CourseService;
import upeldev.com.github.upel3.services.UserService;

import java.security.Principal;
import java.util.List;

@Controller
public class NewActivityController {
    private final UserService userService;
    private final CourseService courseService;
    private final ActivityService activityService;

    @Autowired
    public NewActivityController(UserService userService, CourseService courseService, ActivityService activityService){
        this.userService = userService;
        this.courseService = courseService;
        this.activityService = activityService;
    }

    @RequestMapping(value = "/new_activity")
    public String newCourse(@RequestParam(value = "id")  Long courseId, Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);
        Course currentCourse = courseService.findCourseById(courseId);
        model.addAttribute("course", currentCourse);

        return "new_activity";
    }

    @RequestMapping(value = "/create_activity/{id}")
    public String createCourse(
            @PathVariable("id") Long id,
            @RequestParam(value = "activityName") String activityName,
            @RequestParam(value = "minPoints")  int minValue,
            @RequestParam(value = "maxPoints")  int maxValue,
            Model model,
            Principal principal) {


        User currentUser = userService.findByEmail(principal.getName());
        Course currentCourse = courseService.findCourseById(id);
        model.addAttribute("user", currentUser);

        try{
            if(!currentUser.getRoles().contains(Role.ADMIN) && !currentCourse.getLecturers().contains(currentUser)){
                String errorMsg = "Nie masz wystarczających uprawnień, aby utworzyć kurs";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }
            Activity newActivity = activityService.createActivity(currentCourse, minValue, maxValue, activityName);
            List<Activity> activities =  currentCourse.getActivity();
            model.addAttribute("activities", activities);

            List<Course> courses = currentUser.getCoursesLectured();
            model.addAttribute("courses", courses);
        }
        catch (IllegalArgumentException e){
            String errorMsg = "Podano nieprawidłowe argumenty podczas tworzenia kursu.";
            model.addAttribute("errorMsg", errorMsg);
            return "error";
        }
        return "redirect:/course?id="+id;

    }
}
