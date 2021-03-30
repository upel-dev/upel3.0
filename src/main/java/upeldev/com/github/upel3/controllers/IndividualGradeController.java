package upeldev.com.github.upel3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import upeldev.com.github.upel3.auth.Upel3UserDetails;
import upeldev.com.github.upel3.model.IndividualGrade;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.IndividualGradeService;
import upeldev.com.github.upel3.services.UserService;

import java.util.List;


@Controller
@RequestMapping(path="/individualGrade")
public class IndividualGradeController {
    private final IndividualGradeService individualGradeService;
    private final UserService userService;

    @Autowired
    public IndividualGradeController(IndividualGradeService individualGradeService, UserService userService) {
        this.individualGradeService = individualGradeService;
        this.userService = userService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody
    List<IndividualGrade> getAllGrades(){
        return individualGradeService.findAll();
    }

    @PostMapping("/add")
    public @ResponseBody void postBody(@RequestBody IndividualGrade individualGrade, Authentication authentication) {
        String currentUserEmail = ((Upel3UserDetails) authentication.getPrincipal()).getUsername();
        User user = userService.findByEmail(currentUserEmail);

        if(individualGradeService.canUserAddGrade(individualGrade, user)){
            individualGradeService.save(individualGrade);
        }
    }
}
