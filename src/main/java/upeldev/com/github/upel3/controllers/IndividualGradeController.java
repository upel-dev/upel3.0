package upeldev.com.github.upel3.controllers;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.model.IndividualGrade;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.services.GradeService;
import upeldev.com.github.upel3.services.IndividualGradeService;
import upeldev.com.github.upel3.services.UserService;

import java.util.LinkedHashMap;
import java.util.List;


@Controller
@RequestMapping(path="/individualGrade")
public class IndividualGradeController {
    private final IndividualGradeService individualGradeService;
    private final UserService userService;
    private final GradeService gradeService;

    @Autowired
    public IndividualGradeController(IndividualGradeService individualGradeService, UserService userService, GradeService gradeService) {
        this.individualGradeService = individualGradeService;
        this.userService = userService;
        this.gradeService = gradeService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody
    List<IndividualGrade> getAllGrades(){
        return individualGradeService.findAll();
    }

    @PostMapping("/add")
    public @ResponseBody void postBody(@RequestBody String individualGrade) {

        individualGradeService.save(getNewGradeFromJson(individualGrade));
    }

    private IndividualGrade getNewGradeFromJson(String individualGrade){
        JSONParser parser = new JSONParser(individualGrade);

        LinkedHashMap<String, Object> json = null;
        try {
            json = parser.object();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(json == null){
            return null;
        }
        int gradeId = Integer.parseInt((String)json.get("grade"));

        int userId = Integer.parseInt((String)json.get("user"));
        int gradeValue = Integer.parseInt((String)json.get("value"));

        User user = userService.findById((long)userId);
        Grade grade = gradeService.findGradeById((long)gradeId);

        return new IndividualGrade(user, grade, gradeValue);
    }

}
