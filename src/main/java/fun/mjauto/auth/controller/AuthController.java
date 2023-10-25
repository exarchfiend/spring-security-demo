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
    @GetMapping("/index")
    public String index(){
        return "主页";
    }
}
