package upeldev.com.github.upel3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import upeldev.com.github.upel3.model.Activity;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.CourseService;
import upeldev.com.github.upel3.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class CourseSettingsController {
    private final UserService userService;
    private final CourseService courseService;

    @Autowired
    public CourseSettingsController(UserService userService, CourseService courseService){
        this.userService = userService;
        this.courseService = courseService;
    }

    @RequestMapping(value = "/course_settings", method = RequestMethod.GET)
    public String courseSettings(Model model, Principal principal, HttpServletRequest request) {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);
        try {
            Long id = Long.parseLong(request.getParameter("id").toString());
            Course course = courseService.findCourseById(id);
            model.addAttribute("course", course);

            boolean isHidden = userService.isCourseHidden(currentUser, course);
            model.addAttribute("isHidden", isHidden);
        }
        catch(NumberFormatException nfe) {
            String errorMsg = "Zapytanie GET /course_settings?id=<course_id> otrzymało niewłaściwy typ danych. Spodziewany typ: long integer.";
            model.addAttribute("errorMsg", errorMsg);
            return "error";
        }
        return "course_settings";
    }

    @RequestMapping(value = "/course_settings/hide/{courseId}", method = RequestMethod.GET)
    public String courseSettings(@PathVariable("courseId") Long courseId,
                                 @RequestParam(value = "hide") Long hide,
                                 Model model,Principal principal){
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);

        Course course = courseService.findCourseById(courseId);
        boolean isHidden = userService.isCourseHidden(currentUser, course);

        if(hide == 0 && isHidden){
            userService.showCourse(currentUser, course);
        }
        else if(hide == 1 && !isHidden){
            userService.hideCourse(currentUser, course);
        }
        return "redirect:/course_settings?id="+courseId;
    }

    @RequestMapping(value = "/course_settings/description/{courseId}", method = RequestMethod.POST)
    public String editDescription(@PathVariable("courseId") Long courseId,
                                  @RequestParam(value = "description") String description,
                                  Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());

        Course currentCourse = courseService.findCourseById(courseId);
        model.addAttribute("user", currentUser);

        courseService.changeDescription(currentCourse, description);

        return "redirect:/course_settings?id="+courseId;
    }

    @RequestMapping(value = "/course_settings/name/{courseId}", method = RequestMethod.POST)
    public String changeCourseName(@PathVariable("courseId") Long courseId,
                                     @RequestParam(value = "courseName") String courseName,
                                     Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());

        Course currentCourse = courseService.findCourseById(courseId);
        model.addAttribute("user", currentUser);

        if(!courseName.isEmpty()) {
            courseService.changeName(currentCourse, courseName);
        }

        return "redirect:/course_settings?id="+courseId;
    }
}
