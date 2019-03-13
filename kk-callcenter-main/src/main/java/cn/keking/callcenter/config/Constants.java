package cn.keking.callcenter.config;

/**
 * Created by kl on 2018/11/26.
 * Content : 常量
 */
public class Constants {

    public static final String VIEW_PREFIX = "redirect:/admin/index";

    public static final String REDIRECT_VIEW_LOGIN = VIEW_PREFIX + "/login.html";

    public static final String REDIRECT_VIEW_INDEX = VIEW_PREFIX + "/index.html";

    public static final String APOLLO_FILTER_IGNOREURL_KEY = "callcenter.system.filter.ignoreUrl";
    public static final String APOLLO_FILTER_IGNOREIP_KEY = "callcenter.system.filter.ignoreIp";
    public static final String APOLLO_USERNAME_KEY = "callcenter.system.username";
    public static final String APOLLO_PASSWORD_KEY = "callcenter.system.password";

    public static final String REDIS_QUEUE_NAME = "callcenter-redis_queue_task";

    public static final Integer MAX_RESPONSE_LENTHG = 65535;
}
