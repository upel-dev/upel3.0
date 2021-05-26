package upeldev.com.github.upel3.controllers.new_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.services.ActivityService;
import upeldev.com.github.upel3.services.CourseService;
import upeldev.com.github.upel3.services.SubActivityService;
import upeldev.com.github.upel3.services.UserService;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Controller
public class NewActivityController {
    private final UserService userService;
    private final CourseService courseService;
    private final ActivityService activityService;
    private final SubActivityService subActivityService;

    @Autowired
    public NewActivityController(UserService userService, CourseService courseService, ActivityService activityService, SubActivityService subActivityService){
        this.userService = userService;
        this.courseService = courseService;
        this.activityService = activityService;
        this.subActivityService = subActivityService;
    }

    @RequestMapping(value = "/new_activity/{howMany}")
    public String newCourse(@PathVariable("howMany") int howMany, @RequestParam(value = "id")  Long courseId, Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);
        Course currentCourse = courseService.findCourseById(courseId);
        model.addAttribute("course", currentCourse);
        if(howMany < 1) howMany = 1;
        model.addAttribute("howMany", howMany);

        return "new_templates/new_activity";
    }

    @RequestMapping(value = "/create_activity/{id}")
    public String createCourse(
            @PathVariable("id") Long id,
            @RequestParam(value = "activityName") String activityName,
            @RequestParam(value = "activityDescription") String activityDescription,
            @RequestParam(value = "passValue")  String passValue,
            @RequestParam(value = "subActivity")  String[] subActivityName,
            @RequestParam(value = "maxPoints")  String[] maxPoints,
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

            // This fixes weird bug which occurs when there is only one subactivity and one of the fields is empty
            if(subActivityName.length < 1 || maxPoints.length < 1){
                throw new IllegalArgumentException("No subactiviy");
            }

            int passValueInt;
            int[] maxPointsInt = new int[maxPoints.length];
            try {
                passValueInt = Integer.parseInt(passValue);
                for(int i = 0; i < maxPointsInt.length; i++){
                    maxPointsInt[i] = Integer.parseInt(maxPoints[i]);
                }
            }
            catch (NumberFormatException e){
                throw new IllegalArgumentException("Empty fields");
            }

            Activity newActivity = activityService.createActivity(currentCourse, passValueInt, activityName);
            newActivity.setDescription(activityDescription);
            List<Activity> activities =  currentCourse.getActivity();
            model.addAttribute("activities", activities);

            Set<Course> courses = currentUser.getCoursesLectured();
            model.addAttribute("courses", courses);

            for(int i = 0; i < subActivityName.length; i++){
                SubActivity subActivity = new SubActivity(newActivity, maxPointsInt[i], subActivityName[i]);
                subActivityService.save(subActivity);
            }

        }
        catch (IllegalArgumentException e){
            String errorMsg = "Podano nieprawidłowe argumenty podczas tworzenia kursu.";
            model.addAttribute("errorMsg", errorMsg);
            return "error";
        }
        return "redirect:/course?id="+id;

    }
}
