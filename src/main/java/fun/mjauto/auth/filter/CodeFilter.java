package fun.mjauto.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author MJ
 * @description 验证码过滤器 用于验证验证码
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
        String url = request.getRequestURI(); // 获取请求地址
        String method = request.getMethod(); // 获取请求方法
        // 判断是否为登录请求
        if (url.equals("/login") && method.equals("POST")) {
            // 用户输入的验证码
            String code1 = request.getParameter("captcha");
            // 系统生成的验证码
            String code2 = request.getSession().getAttribute("captcha").toString();
            // 移除Session中系统生成的验证码
            request.getSession().removeAttribute("captcha");
            // 比对验证码是否正确
            return code1.equals(code2);
        }
        // 不是登录请求需要放行
        return true;
    }
}
