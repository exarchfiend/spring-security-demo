package fun.mjauto.auth.controller;

import com.google.code.kaptcha.Producer;
import fun.mjauto.auth.entity.User;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;

/**
 * @author MJ
 * @description
 * @date 2023/10/24
 */
@Slf4j
@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    Producer producer;

    @Autowired
    SessionRegistry sessionRegistry;

    @SneakyThrows
    @GetMapping("/code")
    public void getCode(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        String code = producer.createText();
        log.info("验证码：" + code);

        request.getSession().setAttribute("captcha",code);
        BufferedImage image = producer.createImage(code);
        ServletOutputStream out = response.getOutputStream();

        ImageIO.write(image,"jpg",out);
        out.flush();
    }

    @GetMapping("/index")
    public String index(Model model) {

        // 获取已登录的用户
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

        model.addAttribute("users",allPrincipals);

        return "index";
    }

    @GetMapping("/kickout")
    public String kickOut(String username) {

        // 获取已登录的用户
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

        for (Object principals : allPrincipals){
            List<SessionInformation> allSessions = sessionRegistry.getAllSessions(principals,false);

            User user = (User) principals;

            if (user.getUsername().equals(username)){
                allSessions.forEach(e->e.expireNow()); //将所有已经登录的Session都失效
            }
        }
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
