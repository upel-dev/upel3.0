package upeldev.com.github.upel3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.services.CourseService;
import upeldev.com.github.upel3.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class CourseStudentsController {
    private final UserService userService;
    private final CourseService courseService;

    @Autowired
    public CourseStudentsController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @RequestMapping(value = "/course_students", method = RequestMethod.GET)
    public String studentsInCourseDisplay(
            Model model,
            Principal principal,
            HttpServletRequest request)
    {
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
        return "course_students";
    }

    @RequestMapping(value = "/new_course_students/{courseId}", method = RequestMethod.GET)
    public String addStudentToCurrentCourse(
            @RequestParam(value = "userId") String userId,
            @PathVariable("courseId") Long courseId,
            Model model,
            Principal principal)
    {
        User currentUser = userService.findByEmail(principal.getName());
        Course currentCourse = courseService.findCourseById(courseId);

        model.addAttribute("user", currentUser);
        model.addAttribute("courseId", courseId);

        try {
            if (currentUser.getRoles().contains(Role.STUDENT)){
                String errorMsg = "Nie masz wystarczających uprawnień aby dodać nowego kursanta.";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }

            User newStudent = userService.findByIndexNumber(userId);

            if (newStudent == null)
            {
                String errorMsg = "Niepoprawny numer indeksu.";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }
            if (currentCourse.getEnrolledStudents().contains(newStudent))
            {
                String errorMsg = "Student już jest zapisany do kursu.";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }
            else
                courseService.addStudentToCourse(currentCourse.getId(), newStudent.getId());
        }
        catch (IllegalArgumentException e){
            String errorMsg = "Podano nieprawidłowe argumenty podczas dodawania kursanta.";
            model.addAttribute("errorMsg", errorMsg);
            return "error";
        }
        return "redirect:/course_students?id="+courseId;
    }
}
