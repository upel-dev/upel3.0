package upeldev.com.github.upel3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.Role;
import upeldev.com.github.upel3.model.StudentGroup;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.CourseService;
import upeldev.com.github.upel3.services.StudentGroupService;
import upeldev.com.github.upel3.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
public class GroupController {
    private final UserService userService;
    private final CourseService courseService;
    private final StudentGroupService studentGroupService;

    @Autowired
    public GroupController(UserService userService, CourseService courseService, StudentGroupService studentGroupService) {
        this.userService = userService;
        this.courseService = courseService;
        this.studentGroupService = studentGroupService;
    }


    @RequestMapping(value = "/course_groups", method = RequestMethod.GET)
    public String groupsInCourseDisplay(
            Model model,
            Principal principal,
            HttpServletRequest request)
    {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Course course = courseService.findCourseById(id);
            model.addAttribute("course", course);

            List<StudentGroup> groups = studentGroupService.findByCourse(course);
            model.addAttribute("groups", groups);
        }

        catch(NumberFormatException nfe) {
            String errorMsg = "Zapytanie GET /course?id=<course_id> otrzymało niewłaściwy typ danych. Spodziewany typ: long integer.";
            model.addAttribute("errorMsg", errorMsg);
            return "error";
        }
        return "course_groups";
    }

    @RequestMapping(value = "/course_group_details/{courseId}/{groupId}", method = RequestMethod.GET)
    public String groupDetailsDisplay(
            Model model,
            Principal principal,
            @PathVariable("courseId") Long courseId,
            @PathVariable("groupId") Long groupId)
    {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);

        StudentGroup group = studentGroupService.findById(groupId);
        model.addAttribute("group", group);

        Course course = courseService.findCourseById(courseId);
        model.addAttribute("course", course);

        return "course_group_details";
    }

    @RequestMapping(value = "/new_group_student/{courseId}/{groupId}", method = RequestMethod.GET)
    public String addStudentToCurrentGroup(
            Model model,
            Principal principal,
            @RequestParam(value = "userId") String userId,
            @PathVariable("courseId") Long courseId,
            @PathVariable("groupId") Long groupId)
    {
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);

        Course course = courseService.findCourseById(courseId);
        model.addAttribute("course", course);

        StudentGroup group = studentGroupService.findById(groupId);
        model.addAttribute("group", group);

        try {
            if (!currentUser.getCoursesLectured().contains(course)){
                String errorMsg = "Nie masz wystarczających uprawnień aby dodać nowego kursanta.";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }

            User newStudent = userService.findByIndexNumber(userId);

            if (newStudent == null) {
                String errorMsg = "Niepoprawny numer indeksu.";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }
            else if (group.getStudents().contains(newStudent)) {
                String errorMsg = "Student już jest zapisany do grupy.";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }
            else if (!course.getEnrolledStudents().contains(newStudent)){
                String errorMsg = "Student nie jest zapisany do kursu.";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }
            else
                studentGroupService.addStudentToGroup(group, newStudent);
        }
        catch (IllegalArgumentException e){
            String errorMsg = "Podano nieprawidłowe argumenty podczas dodawania kursanta.";
            model.addAttribute("errorMsg", errorMsg);
            return "error";
        }
        return String.format("redirect:/course_group_details/%d/%d", courseId, groupId);
    }

}
