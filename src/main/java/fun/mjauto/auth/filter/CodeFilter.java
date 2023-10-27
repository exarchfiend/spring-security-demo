package fun.mjauto.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author MJ
 * @description
 * @date 2023/10/26
 */
public class CodeFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 在这里编写验证码验证逻辑
        if (isCaptchaValid(request)) {
            filterChain.doFilter(request, response); // 验证通过，继续执行过滤器链
        } else {
            // 验证失败，可以自定义处理逻辑，比如返回错误信息或重定向到登录页
            logger.info("验证失败");
            // 建议抛出异常 统一错误处理
            response.sendRedirect("/auth/login");
        }
    }

    private boolean isCaptchaValid(HttpServletRequest request) {
        String url = request.getRequestURI();
        String method = request.getMethod();
        logger.info("方法：" + method + " 地址：" + url);
        if (url.equals("/login") && method.equals("POST")) {
            String code1 = request.getParameter("captcha");
            String code2 = request.getSession().getAttribute("captcha").toString();
            logger.info("输入：" + code1 + " 正确：" + code2);
            request.getSession().removeAttribute("captcha");
            return code1.equals(code2);
        }
        return true;
    }
}
