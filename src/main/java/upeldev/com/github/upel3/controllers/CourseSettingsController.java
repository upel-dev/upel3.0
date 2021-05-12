package upeldev.com.github.upel3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @RequestMapping(value = "/course_settings/aggregation_edit/{courseId}", method = RequestMethod.POST)
    public String editAggregation(@PathVariable("courseId") Long courseId,
                                  @ModelAttribute("course") Course course,
                                  Model model, Principal principal){

        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);

        Course currentCourse = courseService.findCourseById(courseId);

        currentCourse.setAggregation(course.getAggregation());
        courseService.save(currentCourse);


        return "redirect:/course_settings?id="+courseId;

    }

    @RequestMapping(value = "/course_settings/passval/{courseId}", method = RequestMethod.POST)
    public String editPassValue(@PathVariable("courseId") Long courseId,
                                  @ModelAttribute("course") Course course,
                                  Model model, Principal principal){

        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);

        Course currentCourse = courseService.findCourseById(courseId);

        currentCourse.setPassValue(course.getPassValue());
        courseService.save(currentCourse);


        return "redirect:/course_settings?id="+courseId;

    }
}
