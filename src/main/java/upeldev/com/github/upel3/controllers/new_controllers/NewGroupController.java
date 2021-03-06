package upeldev.com.github.upel3.controllers.new_controllers;

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

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class NewGroupController {
    private final UserService userService;
    private final CourseService courseService;
    private final StudentGroupService studentGroupService;

    @Autowired
    public NewGroupController(UserService userService, CourseService courseService, StudentGroupService studentGroupService) {
        this.userService = userService;
        this.courseService = courseService;
        this.studentGroupService = studentGroupService;
    }

    @RequestMapping(value = "/new_group/{courseId}", method = RequestMethod.GET)
    public String addGroupPage(
            @PathVariable("courseId") Long courseId,
            Model model,
            Principal principal)
    {
        User currentUser = userService.findByEmail(principal.getName());
        Course course = courseService.findCourseById(courseId);

        model.addAttribute("courseId", courseId);
        model.addAttribute("user", currentUser);
        model.addAttribute("course", course);


        return "new_templates/new_group";
    }

    @RequestMapping(value = "/create_group/{courseId}", method = RequestMethod.GET)
    public String addGroupWithStudents(
            @RequestParam(value = "groupName") String groupName,
            @PathVariable("courseId") Long courseId,
            Model model,
            Principal principal)
    {
        User currentUser = userService.findByEmail(principal.getName());
        Course course = courseService.findCourseById(courseId);
        List<StudentGroup> groups = studentGroupService.findByCourse(course);
        //List<StudentGroup> group = studentGroupService.findByName(groupName);
        Set<User> students = new HashSet<>();
        Long groupId;

        model.addAttribute("courseId", courseId);
        model.addAttribute("user", currentUser);
        model.addAttribute("course", course);
        model.addAttribute("groups", groups);

        try {
            if (!currentUser.getCoursesLectured().contains(course)){
                String errorMsg = "Nie masz wystarczaj??cych uprawnie?? aby stworzy?? now?? grup??.";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }

            if (groupName == null)
            {
                String errorMsg = "Niepoprawny nazwa.";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }
/*            if (groups.contains(group))
            {
                String errorMsg = "Nazwa w u??yciu";
                model.addAttribute("errorMsg", errorMsg);
                return "error";
            }*/
            StudentGroup group = studentGroupService.createStudentGroup(groupName, course, students);
            groupId = group.getId();

        }
        catch (IllegalArgumentException e){
            String errorMsg = "Podano nieprawid??owe argumenty podczas dodawania kursanta.";
            model.addAttribute("errorMsg", errorMsg);
            return "error";
        }
        return String.format("redirect:/course_group_details/%d/%d", courseId, groupId);
    }
}
