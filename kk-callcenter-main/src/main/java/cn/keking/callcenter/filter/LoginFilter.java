package cn.keking.callcenter.filter;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.StandardServletEnvironment;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static cn.keking.callcenter.config.Constants.APOLLO_FILTER_IGNOREURL_KEY;

@Component
@WebFilter(urlPatterns ={"/manager/*","/admin/index/index.html"},filterName = "loginFilter")
public class LoginFilter implements Filter{

    private Environment env;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext context = filterConfig.getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
        env = ctx.getBean(StandardServletEnvironment.class);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestUrl = request.getRequestURI();
        String ignoreUrl = env.getProperty(APOLLO_FILTER_IGNOREURL_KEY);
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        if(session.getAttribute(sessionId) == null && !ignoreUrl.contains(requestUrl)){
             response.sendError(401);
             return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}