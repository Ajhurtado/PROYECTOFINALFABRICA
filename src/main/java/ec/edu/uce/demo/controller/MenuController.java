package ec.edu.uce.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class MenuController {

    @GetMapping("/menu")
    public String index() {
        return "home";
    }
}
