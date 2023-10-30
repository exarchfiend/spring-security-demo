package fun.mjauto.auth.controller;

import com.google.code.kaptcha.Producer;
import fun.mjauto.auth.entity.User;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author MJ
 * @description 认证控制器
 * @date 2023/10/24
 */
@Slf4j
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final Producer producer; // 验证码生成工具配置
    private final SessionRegistry sessionRegistry; // Session仓库

    public AuthController(Producer producer, SessionRegistry sessionRegistry) {
        this.producer = producer;
        this.sessionRegistry = sessionRegistry;
    }

    @GetMapping("/index")
    public String index(Model model) {
        // 获取已登录的用户
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        // 将已登录的用户从控制器传递到视图
        model.addAttribute("users",allPrincipals);
        // 返回首页
        return "index";
    }

    @SneakyThrows
    @GetMapping("/code")
    public void getCode(HttpServletRequest request, HttpServletResponse response) {
        // 设置HTTP响应的Content-Type
        response.setContentType("image/jpeg");
        // 生成一个随机的验证码字符串
        String code = producer.createText();
        // 将生成的验证码字符串记录到日志中
        log.info("验证码：" + code);
        // 将生成的验证码字符串存储在当前会话（Session）中
        request.getSession().setAttribute("captcha",code);
        // 创建一个包含验证码图像的BufferedImage对象
        BufferedImage image = producer.createImage(code);
        // 获取HTTP响应的输出流
        ServletOutputStream out = response.getOutputStream();
        // 将生成的验证码图像写入HTTP响应的输出流
        ImageIO.write(image,"jpg",out);
        // 刷新输出流
        out.flush();
    }

    @GetMapping("/kickout")
    public String kickOut(String username) {
        // 获取已登录的用户
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        // 遍历已登录的用户
        for (Object principals : allPrincipals){
            // 获取用户所有会话 principals是当前正在处理的用户 false参数表示不包括过期的会话
            List<SessionInformation> allSessions = sessionRegistry.getAllSessions(principals,false);
            User user = (User) principals;
            // 操作指定的用户
            if (user.getUsername().equals(username)){
                //将所有用户已经登录的Session都失效
                allSessions.forEach(SessionInformation::expireNow);
            }
        }
        // 返回首页
        return "index";
    }

    @GetMapping("/a/a")
    @ResponseBody
    public String a(){
        return "任意单个字符";
    }

    @GetMapping("/aa/aa")
    @ResponseBody
    public String aa(){
        return "0-任意数量的字符";
    }

    @GetMapping("/aaa/a/b/c")
    @ResponseBody
    public String aaa(){
        return "0-任意数量的目录";
    }
}
