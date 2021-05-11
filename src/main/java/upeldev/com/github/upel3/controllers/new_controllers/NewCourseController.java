package upeldev.com.github.upel3.controllers.new_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.Role;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.CourseService;
import upeldev.com.github.upel3.services.UserService;

import java.security.Principal;
import java.util.Set;

@Controller
public class NewCourseController {
    private final UserService userService;
    private final CourseService courseService;

    @Autowired
    public NewCourseController(UserService userService, CourseService courseService){
        this.userService = userService;
        this.courseService = courseService;
    }

    @RequestMapping(value = "/new_course")
    public String newCourse(Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);
        return "new_templates/new_course";
    }

    @RequestMapping(value = "/create_course")
    public String createCourse(
            @RequestParam(value = "courseName") String courseName,
            @RequestParam(value = "courseDescription") String courseDescription,
            Model model,
            Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());

        model.addAttribute("user", currentUser);

        try{
            if(!currentUser.getRoles().contains(Role.ADMIN) && !currentUser.getRoles().contains(Role.LECTURER)){
                String errorMsg = "Nie masz wystarczających uprawnień, aby utworzyć kurs";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }
            Course newCourse = courseService.createCourse(courseName, courseDescription, currentUser);

            Set<Course> courses = currentUser.getCoursesLectured();
            courses.addAll(currentUser.getCoursesEnrolledIn());
            model.addAttribute("courses", courses);
        }
        catch (IllegalArgumentException e){
            String errorMsg = "Podano nieprawidłowe argumenty podczas tworzenia kursu.";
            model.addAttribute("errorMsg", errorMsg);
            return "error";
        }
        return "redirect:/index";
    }
}
