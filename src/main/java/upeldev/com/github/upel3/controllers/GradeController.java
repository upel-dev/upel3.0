package upeldev.com.github.upel3.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import upeldev.com.github.upel3.model.Grade;
import upeldev.com.github.upel3.services.GradeService;

import java.util.List;

@Controller
@RequestMapping(path="/grade")
public class GradeController {
    private final GradeService gradeService;

    @Autowired
    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody
    List<Grade> getAllGrades(){
        return gradeService.findAll();
    }

    @PostMapping("/add")
    public @ResponseBody void postBody(@RequestBody Grade grade) {
        System.out.println("String " + grade);
        gradeService.save(grade);
    }
}
