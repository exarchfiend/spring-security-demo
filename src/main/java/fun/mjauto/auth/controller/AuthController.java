package fun.mjauto.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author MJ
 * @description
 * @date 2023/10/24
 */
@Controller
@RequestMapping("/auth")
public class AuthController {
    @GetMapping("/code")
    @ResponseBody
    public String getCode(){
        return "获取验证码";
    }

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
