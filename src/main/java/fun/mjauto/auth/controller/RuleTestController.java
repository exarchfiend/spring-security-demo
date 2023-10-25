package fun.mjauto.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RuleTestController {
    @GetMapping("/admin/api")
    public String admin(){
        return "admin";
    }

    @GetMapping("/user/api")
    public String user(){
        return "user";
    }

    @GetMapping("/noAuth")
    public String noAuth(){ return "noAuth"; }

    @GetMapping("/app/api")
    public String index(){ return "app"; }
}
