package fun.mjauto.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author MJ
 * @description
 * @date 2023/10/25
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // 配置请求权限：permitAll()拥有所有权限
        http.authorizeHttpRequests(authorizeHttpRequests ->
                authorizeHttpRequests
                        .requestMatchers("/auth/code").permitAll() // 获取验证码的URL不需要认证
                        .requestMatchers("/login").permitAll() // 登录表单提交处理的URL不需要认证
                        .anyRequest().authenticated() // 其它所有请求都需要认证，不可以匿名访问
        );

        // 配置基于表单的登录认证
        http.formLogin(formLogin -> formLogin
                .loginPage("/auth/login").permitAll() // 自定义登录页面
                .usernameParameter("username") // 用户名字段的参数名
                .passwordParameter("password") // 密码字段的参数名
                .loginProcessingUrl("/login") // 登录表单提交处理的URL
                .defaultSuccessUrl("/auth/index") // 登录成功后的默认URL
        );

        return http.build();
    }
}
