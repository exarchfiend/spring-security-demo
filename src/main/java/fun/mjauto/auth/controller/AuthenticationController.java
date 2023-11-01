package fun.mjauto.auth.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @author MJ
 * @description
 * @date 2023/10/24
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @GetMapping("/code")
    public String getCode(){
        System.out.println("获取验证码");
        return "获取验证码";
    }
}
