package fun.mjauto.auth.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author MJ
 * @description
 * @date 2023/10/25
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }


    // 没用authenticationConfiguration不是spring管理的那个 这是一个新实例
//    private final AuthenticationConfiguration authenticationConfiguration;
//
//    public LoginFilter(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        this.authenticationConfiguration = authenticationConfiguration;
//        super.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());
//    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        logger.info("登陆过滤器...");
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        // 原验证方法
        String username = this.obtainUsername(request);
        username = username != null ? username.trim() : "";
        String password = super.obtainPassword(request);
        password = password != null ? password : "";
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        this.setDetails(request, authRequest);

        // 从请求体中拿数据
//        BufferedReader reader = request.getReader();
//        StringBuffer sbf = new StringBuffer();
//        String line = null;
//        while ((line = reader.readLine()) != null){
//            sbf.append(line);
//        }
//
//        User user = JSONUtil.parseObj(sbf.toString()).toBean(User.class);
//        System.out.println(user);
//        UsernamePasswordAuthenticationToken authRequest =
//                UsernamePasswordAuthenticationToken.unauthenticated(user.getUsername(), user.getPassword());

        // 执行认证
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
