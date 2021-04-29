package upeldev.com.github.upel3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.Role;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.CourseService;
import upeldev.com.github.upel3.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
public class WebController {
    private final UserService userService;
    private final CourseService courseService;

    @Autowired
    public WebController(UserService userService, CourseService courseService){
        this.userService = userService;
        this.courseService = courseService;
    }

    @RequestMapping(value="/logout", method = RequestMethod.POST)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @RequestMapping(value = "/")
    public String defaultPage(Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);

        if(!currentUser.getRoles().contains(Role.ADMIN)) {
            Set<Course> courses = userService.getAllVisibleCourses(currentUser);
            model.addAttribute("courses", courses);
        }
        else {
            List<Course> courses = courseService.findAll();
            model.addAttribute("courses", courses);
        }

        Set<Course> hiddenCourses = userService.getAllHiddenCourses(currentUser);
        model.addAttribute("hiddenCourses", hiddenCourses);

        return "index";
    }

    @RequestMapping(value = "/index")
    public String index(Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);

        if(!currentUser.getRoles().contains(Role.ADMIN)) {
            Set<Course> courses = userService.getAllVisibleCourses(currentUser);
            model.addAttribute("courses", courses);
        }
        else {
            List<Course> courses = courseService.findAll();
            model.addAttribute("courses", courses);
        }

        Set<Course> hiddenCourses = userService.getAllHiddenCourses(currentUser);
        model.addAttribute("hiddenCourses", hiddenCourses);

        return "index";
    }

    @RequestMapping(value = "/course", method = RequestMethod.GET)
    public String courseSpecific(Model model, Principal principal, HttpServletRequest request) {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);
        try {
            Long id = Long.parseLong(request.getParameter("id").toString());
            Course course = courseService.findCourseById(id);
            model.addAttribute("course", course);
        }
        catch(NumberFormatException nfe) {
            String errorMsg = "Zapytanie GET /course?id=<course_id> otrzymało niewłaściwy typ danych. Spodziewany typ: long integer.";
            model.addAttribute("errorMsg", errorMsg);
            return "error";
        }
        return "course";
    }

    @RequestMapping(value = "/course")
    public String course(Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);
        return "course";
    }

    @RequestMapping(value = "/course_settings", method = RequestMethod.GET)
    public String courseSettings(Model model, Principal principal, HttpServletRequest request) {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);
        try {
            Long id = Long.parseLong(request.getParameter("id").toString());
            Course course = courseService.findCourseById(id);
            model.addAttribute("course", course);
        }
        catch(NumberFormatException nfe) {
            String errorMsg = "Zapytanie GET /course_settings?id=<course_id> otrzymało niewłaściwy typ danych. Spodziewany typ: long integer.";
            model.addAttribute("errorMsg", errorMsg);
            return "error";
        }
        return "course_settings";
    }

    @RequestMapping(value = "/course_enrollment")
    public String courseEnrollment(Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);
        return "course_enrollment";
    }

    @RequestMapping(value = "/enroll")
    public String enroll(
            @RequestParam(value = "courseCode") String courseCode, Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);
        try{
            courseService.addStudentToCourseByCode(currentUser, courseCode);
        }
        catch (IllegalArgumentException e){
            String errorMsg = "Niepoprawny kod kursu albo próbujesz się zapisać na kurs, na który już jesteś zapisany.";
            model.addAttribute("errorMsg", errorMsg);
            return "error";
        }
        return "redirect:/index";
    }
}