package fun.mjauto.auth.config;

import fun.mjauto.auth.filter.LoggerRequestFilter;
import fun.mjauto.auth.filter.VerificationCodeFilter;
import fun.mjauto.auth.security.AuthenticationFailureHandlerImpl;
import fun.mjauto.auth.security.AuthenticationSuccessHandlerImpl;
import fun.mjauto.auth.security.LogoutSuccessHandlerImpl;
import fun.mjauto.auth.service.impl.PersistentTokenRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;

/**
 * @author MJ
 * @description
 * @date 2023/10/31
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PersistentTokenRepositoryImpl persistentTokenRepositoryImpl;

    public SecurityConfig(PersistentTokenRepositoryImpl persistentTokenRepositoryImpl) {
        this.persistentTokenRepositoryImpl = persistentTokenRepositoryImpl;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 配置访问权限：permitAll()拥有所有权限
        http.authorizeHttpRequests(authorizeHttpRequests->
                        authorizeHttpRequests
                                .requestMatchers("/login").permitAll() // 获取验证码的URL不需要认证
                                .requestMatchers("/auth/code").permitAll() // 其它所有请求都需要认证，不可以匿名访问
                                .anyRequest().authenticated() // 其它所有请求都需要认证，不可以匿名访问
                );

        // 配置基于表单的登录认证
        http.formLogin(formLogin->
                formLogin
                        .loginProcessingUrl("/login") // 登录表单提交处理的URL
                        .successHandler(new AuthenticationSuccessHandlerImpl()) // 登录成功处理器
                        .failureHandler(new AuthenticationFailureHandlerImpl()) // 登录失败处理器
        );

        // 配置记住我
        http.rememberMe(rememberMe -> rememberMe
                .rememberMeCookieName("rememberMe") // 默认是remember-me
                .rememberMeParameter("rememberMe") // 默认是remember-me
                .key("myKey") // 加密密钥
                .tokenRepository(this.persistentTokenRepositoryImpl) // 接口PersistentTokenRepository的默认的现类JdbcTokenRepositoryImpl
        );

        // 配置注销登录
        http.logout(logout->
                logout
                        .logoutUrl("/logout") // 注销登录的URL
                        .deleteCookies("rememberMe") // 清除cookies
                        .logoutSuccessHandler(new LogoutSuccessHandlerImpl()) // 退出成功处理器
        );

        // 配置自定义第一个过滤器
        http.addFilterBefore(new LoggerRequestFilter(), DisableEncodeUrlFilter.class);

        // 配置自定义验证码过滤器
        http.addFilterBefore(new VerificationCodeFilter(), UsernamePasswordAuthenticationFilter.class);

        // 关闭跨域漏洞防御
        http.csrf(AbstractHttpConfigurer::disable);

        // 关闭跨域拦截
        http.cors(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // 明文加密
        return NoOpPasswordEncoder.getInstance();
    }
}
