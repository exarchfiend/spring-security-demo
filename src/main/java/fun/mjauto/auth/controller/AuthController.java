package fun.mjauto.auth.controller;

import fun.mjauto.auth.entity.CurrentUser;
import fun.mjauto.auth.entity.CustomUser;
import org.springframework.http.ResponseEntity;
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
    public String getCode(){
        return "获取验证码";
    }

    @GetMapping("/index")
    public String index(){
        return "主页";
    }
}
