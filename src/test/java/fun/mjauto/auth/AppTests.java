package fun.mjauto.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author MJ
 * @description 测试启动类
 * @date 2023/10/24
 */
@SpringBootTest
class AppTests {
    @Test
    void contextLoads() {
        // 认证加密方式
        String password = "123456";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        System.out.println("原密码: " + password);
        System.out.println("加密后的密码: " + hashedPassword);
    }
}
