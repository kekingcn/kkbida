package cn.keking.callcenter.sdk.exception;

/**
 * @auther: chenjh
 * @time: 2018/11/30 11:03
 * @description
 */
public class CallBackException extends RuntimeException {
    public CallBackException() {
        super("回调异常");
    }

    public CallBackException(String msg) {
        super(msg);
    }
}
