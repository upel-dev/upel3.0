package upeldev.com.github.upel3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.CourseService;
import upeldev.com.github.upel3.services.GradeService;
import upeldev.com.github.upel3.services.UserService;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Controller
public class LeaderboardController {
    private final UserService userService;
    private final CourseService courseService;
    private final GradeService gradeService;

    @Autowired
    public LeaderboardController(UserService userService, CourseService courseService, GradeService gradeService){
        this.userService = userService;
        this.courseService = courseService;
        this.gradeService = gradeService;
    }

    @RequestMapping(value = "/leaderboard/{courseID}",  method = RequestMethod.GET)
    public String leaderboardDisplay(
            @PathVariable("courseID") Long courseId,
            Model model,
            Principal principal)
    {
        List<StringBuilder> gradeList = new LinkedList<>();
        List<String> userList = new LinkedList<>();
        StringBuilder gradeString;
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);

        Course currentCourse = courseService.findCourseById(courseId);
        model.addAttribute("course", currentCourse);

        Set<User> users = currentCourse.getEnrolledStudents();

        for (User user : users) {
            List<Grade> grades = gradeService.findGradeByCourseAndUser(currentCourse, user);
            gradeString = new StringBuilder();
            for (Grade grade : grades){
                String g = String.valueOf(grade.getValue());
                gradeString.append(g).append(", ");
            }
            gradeList.add(gradeString);
            userList.add(user.getEmail());
        }
        model.addAttribute("userList", userList);
        model.addAttribute("gradeList", gradeList);

        return "redirect:/leaderboard?id="+courseId;
    }
}
