package fun.mjauto.auth.controller;

import com.google.code.kaptcha.Producer;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

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
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
