package fun.mjauto.auth.config;

import fun.mjauto.auth.exception.LoginFailureHandler;
import fun.mjauto.auth.filter.CodeFilter;
import fun.mjauto.auth.filter.LoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
        // 配置请求安全权限：permitAll()拥有所有权限
        http.authorizeHttpRequests(authorizeHttpRequests ->
                authorizeHttpRequests
                        // 系统角色相关
                        //.requestMatchers("/admin/api").hasRole("admin") // 角色admin可以访问
                        //.requestMatchers("/user/api").hasAnyRole("admin","user") // 角色admin和user可以访问
                        // 系统权限相关
                        .requestMatchers("/admin/api").hasAnyAuthority("admin:api") // 权限admin可以访问
                        .requestMatchers("/user/api").hasAnyAuthority("admin:api","user:api") // 权限admin和user可以访问
                        // 匹配相关相关
                        .requestMatchers("/admin/a/?").hasAnyAuthority("admin:api") // 任意单个字符
                        .requestMatchers("/admin/aa/*").hasAnyAuthority("admin:api") // 0-任意数量的字符
                        .requestMatchers("/admin/aaa/**").hasAnyAuthority("admin:api") // 0-任意数量的目录

                        .requestMatchers("/auth/code").permitAll() // 获取验证码的URL不需要认证
//                        .requestMatchers("/favicon.ico").permitAll() // 浏览器默认发送的请求
                        .requestMatchers("/login").permitAll() // 登录表单提交处理的URL不需要认证
                        .anyRequest().authenticated() // 其它所有请求都需要认证，不可以匿名访问
        );

        // 异常处理配置一个未授权页面
        http.exceptionHandling(exceptionHandling->
                exceptionHandling
                        .accessDeniedPage("/noAuth")
        );
        // 异常处理配置一个未授权处理器
//        http.exceptionHandling(exceptionHandling->
//                exceptionHandling
//                        .accessDeniedHandler(new AccessDeniedHandler() {
//                            @Override
//                            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//                                System.out.println("未授权页面");
//                                accessDeniedException.printStackTrace();
//                            }
//                        })
//        );

        // 配置基于表单的登录认证
        http.formLogin(formLogin ->
                formLogin
                        .loginPage("/auth/login").permitAll() // 自定义登录页面
                        .usernameParameter("username") // 用户名字段的参数名
                        .passwordParameter("password") // 密码字段的参数名
                        .loginProcessingUrl("/login") // 登录表单提交处理的URL
                        .defaultSuccessUrl("/auth/index") // 登录成功后的默认URL
                        .failureHandler(new LoginFailureHandler()) // 登录失败处理器
        );

        // 配置自定义登录过滤器
//        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterAt(new LoginFilter(authenticationConfiguration), UsernamePasswordAuthenticationFilter.class); // 没用
        // 配置验证码拦截器
        http.addFilterBefore(new CodeFilter(), UsernamePasswordAuthenticationFilter.class);

        // 配置退出
        http.logout(logout->
                logout
                        .invalidateHttpSession(true) // 让Session失效
        );

        // 关闭跨域漏洞防御
        http.csrf(Customizer.withDefaults());

        return http.build();
    }

//    private final DataSource dataSource;
//
//    @Autowired
//    public SecurityConfig(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        // 引入dataSource
//        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
//        userDetailsManager.setDataSource(dataSource);
//
//        // 设置管理员和用户角色
////        UserDetails user1 = User.withUsername("admin").password("123456").roles("admin","user").build();
////        UserDetails user2 = User.withUsername("user").password("123456").roles("user").build();
//
//        // 设置管理员和用户权限
//        UserDetails user1 = User.withUsername("admin").password("123456").authorities("admin:api","user:api").build();
//        UserDetails user2 = User.withUsername("user").password("123456").authorities("user:api").build();
//
//        // 在表里面创建用户信息
//        if (!userDetailsManager.userExists("admin") && !userDetailsManager.userExists("user")){
//            userDetailsManager.createUser(user1);
//            userDetailsManager.createUser(user2);
//        }
//
//        return new InMemoryUserDetailsManager(user1,user2);
//    }

//    @Bean
//    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
//        // 设置管理员和用户角色
//        // UserDetails user1 = User.withUsername("admin").password("123456").roles("admin","user").build();
//        // UserDetails user2 = User.withUsername("user").password("123456").roles("user").build();
//
//        // 设置管理员和用户权限
//        UserDetails user1 = User.withUsername("admin").password("123456").authorities("admin:api","user:api").build();
//        UserDetails user2 = User.withUsername("user").password("123456").authorities("user:api").build();
//
//        // 总结 角色和权限其实是一样的 角色会被加上前缀：ROLE_
//        return new InMemoryUserDetailsManager(user1,user2);
//    }

    @Autowired
    AuthenticationConfiguration authenticationConfiguration;

//     配置自定义登录过滤器
    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
//        loginFilter.setAuthenticationFailureHandler(new LoginFailureHandler());
//        loginFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler());

        loginFilter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());
        return loginFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // 明文加密
        return NoOpPasswordEncoder.getInstance();
    }
}
