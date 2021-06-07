package upeldev.com.github.upel3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import upeldev.com.github.upel3.model.Course;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.CourseService;
import upeldev.com.github.upel3.services.GradeService;
import upeldev.com.github.upel3.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

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

    @RequestMapping(value = "/leaderboard_lecturer",  method = RequestMethod.GET)
    public String leaderboardFullDisplay(
            Model model,
            Principal principal,
            HttpServletRequest request)
    {
        Long id = Long.parseLong(request.getParameter("id"));
        Course currentCourse = courseService.findCourseById(id);
        model.addAttribute("course", currentCourse);

        Map<Double, String> userGradeMap = new HashMap<Double, String>();
        
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);

        Set<User> users = currentCourse.getEnrolledStudents();

        for (User user : users) {
            double userValue = courseService.getUserValueInCourse(currentCourse, user);
            userGradeMap.put(userValue, user.getEmail());
        }

        Map<Double, String> sortedUserGradeMap = new TreeMap<Double, String>(userGradeMap).descendingMap();
        model.addAttribute("studentGrades", sortedUserGradeMap);

        return "leaderboard_lecturer";
    }

    @RequestMapping(value = "/leaderboard_student",  method = RequestMethod.GET)
    public String leaderboardSingleDisplay(
            Model model,
            Principal principal,
            HttpServletRequest request)
    {
        Long id = Long.parseLong(request.getParameter("id"));
        Course currentCourse = courseService.findCourseById(id);
        model.addAttribute("course", currentCourse);

        Map<Double, String> userGradeMap = new HashMap<Double, String>();

        double currentUserValue = 0;
        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);

        Set<User> users = currentCourse.getEnrolledStudents();

        for (User user : users) {
            double userValue = courseService.getUserValueInCourse(currentCourse, user);
            userGradeMap.put(userValue, user.getEmail());
            if(currentUser == user)
                currentUserValue = userValue;
        }
        NavigableMap<Double, String> sortedUserGradeMap = new TreeMap<Double, String>(userGradeMap).descendingMap();

        int leaderboardNumber = 0;
        leaderboardNumber = sortedUserGradeMap.headMap(currentUserValue).size() + 1;
        model.addAttribute("number", leaderboardNumber);
        model.addAttribute("sum", currentUserValue);

        return "leaderboard_student";
    }
}
