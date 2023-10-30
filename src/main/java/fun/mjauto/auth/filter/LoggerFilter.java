package fun.mjauto.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author MJ
 * @description 日志过滤器 打印请求信息
 * @date 2023/10/30
 */
public class LoggerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI(); // 获取请求地址
        String method = request.getMethod(); // 获取请求方法
        logger.info("方法：" + method + " 地址：" + url); // 打印请求信息日志
        filterChain.doFilter(request, response); // 继续执行过滤器链
    }
}
