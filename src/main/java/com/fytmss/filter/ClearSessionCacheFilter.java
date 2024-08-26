package com.fytmss.filter;

import com.fytmss.common.utils.ServletUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;

import java.io.IOException;

/**
 * @author wgq
 * @create 2024/8/14-周三 10:29
 * 当用户在前端调用 ajax 的请求时，无法将此时 session 过期的状态返回给界面，所以下面还需要加监听。
 */
public class ClearSessionCacheFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String basePath = request.getContextPath();
        request.setAttribute("basePath", basePath);
        // 判断 session 里是否有用户信息
        // todo 设置了 session-status 响应头，并且没有继续执行过滤链。这可能会导致请求被终止，从而不能正确地处理跨域配置。
//        if (!SecurityUtils.getSubject().isAuthenticated()) {
//            // 如果是ajax请求响应头会有，x-requested-with
//            if (ServletUtils.isAjaxRequest(request)) {
//                // 在响应头设置session状态
//                response.setHeader("session-status", "timeout");
//                return;
//            }
//        }
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {

    }
}