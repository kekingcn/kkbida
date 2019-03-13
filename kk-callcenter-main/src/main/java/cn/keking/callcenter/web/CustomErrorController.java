package cn.keking.callcenter.web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static cn.keking.callcenter.config.Constants.REDIRECT_VIEW_LOGIN;

/**
 * Created by kl on 2018/11/26.
 * Content :自定义服务器内部错误处理页面
 */
@Controller
class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request){
        return REDIRECT_VIEW_LOGIN;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}