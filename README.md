# spring-security-demo

## 过滤器链代理FilterChainProxy的doFilterInternal方法里面包含全部的过滤器  
filters = {ArrayList@8826} size = 13  
0 =(DisableEncodeUrlFilter@10554}  
1 = {WebAsyncManagerIntegrationFilter@10555}  
2 = (SecurityContextHolderFilter@10556)  
3 = (HeaderWriterFilter@10557}  
4 ={CsrfFilter@10558}  
5 =(LogoutFilter@10559}  
6 ={CodeFilter@10560}  
7= (UsernamePasswordAuthenticationFilter@10561}  
8 = {RequestCacheAwareFilter@10562)  
9 ={SecurityContextHolderAwareRequestFilter@10563}  
10 ={(AnonymousAuthenticationFilter@10564)  
11 ={ExceptionTranslationFilter@10565}  
12={AuthorizationFilter@10566}  

# 下面是关于各个过滤器的介绍：  
DisableEncodeUrlFilter：用于禁用URL编码。  
WebAsyncManagerIntegrationFilter：与Spring的WebAsyncManager集成相关的过滤器。  
SecurityContextHolderFilter：在安全上下文中存储身份验证信息，以便后续过滤器和请求处理器可以访问它。  
HeaderWriterFilter：用于设置HTTP响应头，例如安全相关的头，以提高Web应用的安全性。  
CsrfFilter：用于防止跨站请求伪造攻击（CSRF）。  
LogoutFilter：处理用户注销的过滤器。  
CodeFilter：自定义的处理验证码验证的过滤器。  
UsernamePasswordAuthenticationFilter：处理基于用户名和密码的身份验证。（自定义登录认证过滤器继承此过滤器）  
RequestCacheAwareFilter：支持请求缓存，通常用于处理身份验证过程中的重定向。  
SecurityContextHolderAwareRequestFilter：在请求对象中提供对安全上下文的访问。  
AnonymousAuthenticationFilter：如果请求未经身份验证，则为请求创建匿名身份验证。  
ExceptionTranslationFilter：处理身份验证和访问拒绝时的异常，并提供适当的响应。  
AuthorizationFilter：处理请求的授权（访问权限）部分，以确保用户是否有权限执行该请求。  


## 配置请求安全权限  
http.authorizeHttpRequests(authorizeHttpRequests->authorizeHttpRequests.***);  
# 系统角色：  
.requestMatchers("/admin/api").hasRole("admin") // 角色admin可以访问  
.requestMatchers("/user/api").hasAnyRole("admin","user") // 角色admin和user可以访问  
# 系统权限：  
.requestMatchers("/admin/api").hasAnyAuthority("admin:api") // 权限admin可以访问  
.requestMatchers("/user/api").hasAnyAuthority("admin:api","user:api") // 权限admin和user可以访问  
# url匹配：  
.requestMatchers("/admin/a/?").hasAnyAuthority("admin:api") // 任意单个字符  
.requestMatchers("/admin/aa/*").hasAnyAuthority("admin:api") // 0-任意数量的字符  
.requestMatchers("/admin/aaa/**").hasAnyAuthority("admin:api") // 0-任意数量的目录  
# 全权限：  
.requestMatchers("/auth/code").permitAll() // 获取验证码的URL不需要认证  
.requestMatchers("/login").permitAll() // 登录表单提交处理的URL不需要认证  
# 无权限：  
.anyRequest().authenticated() // 其它所有请求都需要认证，不可以匿名访问  

## 配置异常处理  
http.exceptionHandling(exceptionHandling->exceptionHandling.***);  
# 未授权页面：  
.accessDeniedPage("/noAuth")  
# 未授权处理器：  
.accessDeniedHandler(new AccessDeniedHandler() {})  

## 配置基于表单的登录认证  
http.formLogin(formLogin->formLogin.***);  
.loginPage("/auth/login").permitAll() // 自定义登录页面  
.usernameParameter("username") // 用户名字段的参数名  
.passwordParameter("password") // 密码字段的参数名  
.loginProcessingUrl("/login") // 登录表单提交处理的URL  
.defaultSuccessUrl("/auth/index") // 登录成功后的默认URL  
.failureHandler(new LoginFailureHandler()) // 登录失败处理器  

## 配置自定义拦截器  
http.addFilterBefore(new CodeFilter(), UsernamePasswordAuthenticationFilter.class);  
# 验证码拦截器：  
new CodeFilter() // 拦截器  
UsernamePasswordAuthenticationFilter.class // 相对拦截器  
addFilterBefore() // 位置(之前)  
addFilterAt() // 位置(替代)  
addFilterAfter() // 位置(之后)  

## 关闭跨域漏洞防御  
http.csrf(Customizer.withDefaults());  

## 配置退出  
http.logout(logout->logout);  
.invalidateHttpSession(true) // 让Session失效  

## 配置自定义登录过滤器  
@Autowired  
AuthenticationConfiguration authenticationConfiguration; // 拿到安全配置  
@Bean  
public LoginFilter loginFilter() throws Exception {  
LoginFilter loginFilter = new LoginFilter();  
//loginFilter.setAuthenticationFailureHandler(new LoginFailureHandler()); // 失败处理器  
//loginFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler()); // 成功处理器  
loginFilter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager()); // 配置身份验证管理器  
return loginFilter;  
}  

## 配置密码加密方式  
@Bean
public PasswordEncoder passwordEncoder(){  
//明文加密  
return NoOpPasswordEncoder.getInstance();  
}  
