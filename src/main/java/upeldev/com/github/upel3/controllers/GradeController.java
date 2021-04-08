package upeldev.com.github.upel3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import upeldev.com.github.upel3.auth.Upel3UserDetails;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.GradeService;
import upeldev.com.github.upel3.services.UserService;

import java.util.List;


@Controller
@RequestMapping(path="/grade")
public class GradeController {
    private final GradeService gradeService;
    private final UserService userService;

    @Autowired
    public GradeController(GradeService gradeService, UserService userService) {
        this.gradeService = gradeService;
        this.userService = userService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody
    List<Grade> getAllGrades(){
        return gradeService.findAll();
    }

    @PostMapping("/add")
    public @ResponseBody void postBody(@RequestBody Grade grade, Authentication authentication) {
        String currentUserEmail = ((Upel3UserDetails) authentication.getPrincipal()).getUsername();
        User user = userService.findByEmail(currentUserEmail);

        if(gradeService.canUserAddGrade(grade, user)){
            gradeService.save(grade);
        }
    }
}
