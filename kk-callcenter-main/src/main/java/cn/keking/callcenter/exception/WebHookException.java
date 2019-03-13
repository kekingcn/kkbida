package cn.keking.callcenter.exception;

/**
 * @auther: chenjh
 * @time: 2019/3/4 15:13
 * @description
 */
public class WebHookException extends RuntimeException {

    public WebHookException() {
        super("webhook通知异常");
    }

    public WebHookException(String msg) {
        super("webhook通知异常，" + msg);
    }

}
