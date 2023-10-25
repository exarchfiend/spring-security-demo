package fun.mjauto.auth.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @author MJ
 * @description
 * @date 2023/10/24
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    @GetMapping("/code")
    public String index(){
        return "获取验证码";
    }
}
