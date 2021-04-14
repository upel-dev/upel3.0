package upeldev.com.github.upel3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import upeldev.com.github.upel3.model.*;
import upeldev.com.github.upel3.services.CourseService;
import upeldev.com.github.upel3.services.UserService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
public class StudentToCourseController {
    private final UserService userService;
    private final CourseService courseService;

    @Autowired
    public StudentToCourseController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @RequestMapping(value = "/course_users", method = RequestMethod.GET)
    public String studentsInCourse(
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
        return "course_users";
    }

    @ModelAttribute("users")
    public List<User> users() {
        return userService.findAllStudents();
    }

    @RequestMapping(value = "/new_users/{courseId}", method = RequestMethod.GET)
    public String addStudent(
            @RequestParam(value = "userName") String userName,
            @PathVariable("courseId") Long courseId,
            Model model,
            Principal principal/*,
            HttpServletRequest requestID,
            HttpServletRequest requestName*/)
    {
        User currentUser = userService.findByEmail(principal.getName());

        //Long courseId = Long.parseLong(requestID.getParameter("id"));
        Course currentCourse = courseService.findCourseById(courseId);

        model.addAttribute("user", currentUser);
        model.addAttribute("courseId", courseId);


        try{
            if(!currentUser.getRoles().contains(Role.ADMIN) && !currentUser.getRoles().contains(Role.LECTURER)){
                String errorMsg = "Nie masz wystarczających uprawnień, aby dodać kursanta";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }


/*            users = userService.findAllStudents();
            users.removeAll(currentCourse.getEnrolledStudents());

            String userName = requestName.getParameter("userName");*/

            User newStudent = userService.findByEmail(userName);
            if (currentCourse.getEnrolledStudents().contains(newStudent))
            {
                String errorMsg = "Student juz zapisany do kursu";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }
            else
                courseService.addStudentToCourse(currentCourse, newStudent);
        }
        catch (IllegalArgumentException e){
            String errorMsg = "Podano nieprawidłowe argumenty podczas dodawania kursanta.";
            model.addAttribute("errorMsg", errorMsg);
            return "error";
        }

        return "redirect:/course_users?id="+courseId;
    }
}
