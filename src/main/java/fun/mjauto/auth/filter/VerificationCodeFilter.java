package fun.mjauto.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author MJ
 * @description
 * @date 2023/10/31
 */
@Data
public class VerificationCodeFilter extends OncePerRequestFilter {

    private String captchaCodeParameter = "captchaCode";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("验证码过滤器...");

        String username = request.getParameter("username");
        username = username != null ? username.trim() : "";

        String password = request.getParameter("password");
        password = password != null ? password : "";

        String captchaCode = this.obtainCaptchaCode(request);
        captchaCode = captchaCode != null ? captchaCode.trim() : "";

        String rememberMe = request.getParameter("rememberMe");
        rememberMe = rememberMe != null ? rememberMe.trim() : "";

        logger.info(username + password + captchaCode + rememberMe);
        filterChain.doFilter(request,response);
    }

    @Nullable
    protected String obtainCaptchaCode(HttpServletRequest request) {
        return request.getParameter(this.captchaCodeParameter);
    }
}
