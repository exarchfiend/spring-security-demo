package fun.mjauto.auth.config;

import fun.mjauto.auth.exception.LoginFailureHandler;
import fun.mjauto.auth.filter.CodeFilter;
import fun.mjauto.auth.filter.LoggerFilter;
import fun.mjauto.auth.filter.LoginFilter;
import fun.mjauto.auth.service.impl.TokenServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;

/**
 * @author MJ
 * @description Security配置类
 * @date 2023/10/25
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TokenServiceImpl tokenServiceImpl;
    private final AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfig(TokenServiceImpl tokenServiceImpl, AuthenticationConfiguration authenticationConfiguration) {
        this.tokenServiceImpl = tokenServiceImpl;
        this.authenticationConfiguration = authenticationConfiguration;
    }

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
                        .requestMatchers("/auth/a/?").hasAnyAuthority("admin:api") // 任意单个字符
                        .requestMatchers("/auth/aa/*").hasAnyAuthority("admin:api") // 0-任意数量的字符
                        .requestMatchers("/auth/aaa/**").hasAnyAuthority("admin:api") // 0-任意数量的目录

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
                        .loginPage("/sys/login").permitAll() // 自定义登录页面
                        .usernameParameter("username") // 用户名字段的参数名
                        .passwordParameter("password") // 密码字段的参数名
                        .loginProcessingUrl("/login") // 登录表单提交处理的URL
                        .defaultSuccessUrl("/auth/index") // 登录成功后的默认URL
                        .failureHandler(new LoginFailureHandler()) // 登录失败处理器
        );

        // 配置自定义登录过滤器
//        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);

        // 配置自定义验证码拦截器
        http.addFilterBefore(new CodeFilter(), UsernamePasswordAuthenticationFilter.class);

        // 配置自定义第一个拦截器
        http.addFilterBefore(new LoggerFilter(), DisableEncodeUrlFilter.class);

        // 配置退出
        http.logout(logout->
                logout
                        .invalidateHttpSession(true) // 让Session失效
                        .deleteCookies("rememberMe") // 清除cookies
                        .logoutSuccessUrl("/sys/login") // 成功跳转url(可以配置自定义退出提示界面)
        );

        // 关闭跨域漏洞防御
        http.csrf(Customizer.withDefaults());

        // 配置记住我
        http.rememberMe(rememberMe -> rememberMe
                .rememberMeCookieName("rememberMe") // 默认是remember-me
                .rememberMeParameter("rememberMe") // 默认是remember-me
                .key("myKey") // 加密密钥
                .tokenRepository(tokenServiceImpl) // 接口PersistentTokenRepository的默认的现类JdbcTokenRepositoryImpl
        );

        // 会话失效策略
//        http.sessionManagement(sessionManagement->
//                sessionManagement
//                        .invalidSessionStrategy(new InvalidSessionStrategy() {
//                            @Override
//                            public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//                                response.setContentType("text/html;charset=UTF-8");
//                                response.getWriter().write("会话失效");
//                            }
//                        }) // 会话失效策略
//        );

        // 会话失效策略
        http.sessionManagement(sessionManagement->
                sessionManagement
                        .invalidSessionUrl("/auth/login")
                        .maximumSessions(1)
                        .sessionRegistry(sessionRegistry())
        );

        return http.build();
    }

    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
//        loginFilter.setAuthenticationFailureHandler(new LoginFailureHandler());
//        loginFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
        // 设置身份验证管理器（Authentication Manager）
        loginFilter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());
        return loginFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // 明文加密
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SessionRegistry sessionRegistry(){
        // 获取已登录用户
        return new SessionRegistryImpl();
    }

    //    private final DataSource dataSource;
//
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
}
