package cn.keking.callcenter.filter;

import cn.keking.callcenter.utils.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.StandardServletEnvironment;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cn.keking.callcenter.config.Constants.*;

@Component
@WebFilter(urlPatterns ={"/api/*"},filterName = "apiFilter")
public class ApiFilter implements Filter{

    Logger logger = LoggerFactory.getLogger(getClass());
    private Environment env;

    @Override
    public void init(FilterConfig filterConfig) {
        ServletContext context = filterConfig.getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
        env = ctx.getBean(StandardServletEnvironment.class);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String ip = WebUtil.getRequestIp(request);
        String username = request.getHeader("username");
        String password = request.getHeader("password");
        if(!env.getProperty(APOLLO_FILTER_IGNOREIP_KEY).contains(ip)){
            logger.info(ip + "未加白名单，已拒绝");
            response.setStatus(403);
            return;
        }
        if(!env.getProperty(APOLLO_USERNAME_KEY).equals(username) || !env.getProperty(APOLLO_PASSWORD_KEY).equals(password)){
            logger.info(ip + "用户名或密码错误，已拒绝");
            response.setStatus(401);
            return;
        }
        filterChain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}