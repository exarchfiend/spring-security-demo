package fun.mjauto.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
                        .requestMatchers("/admin/api").hasRole("admin") // 角色admin可以访问
                        .requestMatchers("/user/api").hasAnyRole("admin","user") // 角色admin和user可以访问
                        .requestMatchers("/auth/code").permitAll() // 获取验证码的URL不需要认证
                        .requestMatchers("/login").permitAll() // 登录表单提交处理的URL不需要认证
                        .anyRequest().authenticated() // 其它所有请求都需要认证，不可以匿名访问
        );

        // 异常处理配置一个未授权页面
        http.exceptionHandling(exceptionHandling->
                exceptionHandling
                        .accessDeniedPage("/noAuth")
        );

        // 配置基于表单的登录认证
        http.formLogin(formLogin ->
                formLogin
                        .loginPage("/auth/login").permitAll() // 自定义登录页面
                        .usernameParameter("username") // 用户名字段的参数名
                        .passwordParameter("password") // 密码字段的参数名
                        .loginProcessingUrl("/login") // 登录表单提交处理的URL
                        .defaultSuccessUrl("/auth/index") // 登录成功后的默认URL
        );

        // 配置退出
        http.logout(logout->
                logout
                        .invalidateHttpSession(true)); // 让Session失效

        // 关闭跨域漏洞防御
        http.csrf(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        // 设置一个管理员角色
        UserDetails user1 = User.withUsername("admin").password("123456").roles("admin","user").build();
        // 设置一个用户角色
        UserDetails user2 = User.withUsername("user").password("123456").roles("user").build();
        return new InMemoryUserDetailsManager(user1,user2);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // 明文加密
        return NoOpPasswordEncoder.getInstance();
    }
}
