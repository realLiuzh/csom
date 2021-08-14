package com.hlx.csom.filter;/**
 * @Author lzh
 */

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName CrosFilter
 * @Description //TODO
 * @Author lzh
 * @Date 2021/3/6 23:00
 * @Version 1.0
 */
@Component
@WebFilter(urlPatterns = "/**",filterName = "crosFilter")
public class CrosFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws  ServletException, IOException {
        HttpServletResponse rsp = (HttpServletResponse) resp;
        rsp.setHeader("Access-Control-Allow-Origin","*");
        rsp.setHeader("Access-Control-Allow-Methods","POST,GET,DELETE,PUT");
        rsp.setHeader("Access-Control-Max-Age","3600");
        rsp.setHeader("Access-Control-Allow-Headers","*");
        filterChain.doFilter(req,resp);
    }

    @Override
    public void destroy() {

    }
}
