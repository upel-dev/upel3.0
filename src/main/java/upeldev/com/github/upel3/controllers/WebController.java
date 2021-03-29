package upeldev.com.github.upel3.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/user")
    public String users() {
        return "users";
    }

    @RequestMapping(value = "/grade")
    public String grade() {
        return "grade";
    }

    @RequestMapping(value = "/individualGrade")
    public String individualGrade() {
        return "individualGrade";
    }
}
