package fun.mjauto.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
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

    @GetMapping("/admin/a/a")
    @ResponseBody
    public String a(){
        return "任意单个字符";
    }

    @GetMapping("/admin/aa/aa")
    @ResponseBody
    public String aa(){
        return "0-任意数量的字符";
    }

    @GetMapping("/admin/aaa/a/b/c")
    @ResponseBody
    public String aaa(){
        return "0-任意数量的目录";
    }
}
