package fun.mjauto.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * @author MJ
 * @description 页面控制器
 * @date 2023/10/24
 */
@Controller
public class PageController {
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
    public String app(){ return "app"; }

    @GetMapping("/sys/login")
    public String login() { return "login"; }
}
