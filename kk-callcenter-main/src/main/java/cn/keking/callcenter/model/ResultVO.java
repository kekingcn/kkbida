package cn.keking.callcenter.model;

/**
 * @auther: chenjh
 * @time: 2018/12/4 9:13
 * @description
 */
public class ResultVO {

    private static final long serialVersionUID = 1L;

    public static final Integer OK = 0;
    public static final Integer ERROR = 1;

    private Integer code;//1 失败  0 成功

    private String msg;//code为1的时候返回 错误字符串

    private Object data;//code为0的时候返回 业务数据

    public ResultVO() {

    }

    public ResultVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultVO(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static ResultVO putSuccess() {
        return new ResultVO(OK, "成功");
    }

    public static ResultVO putSuccess(Object data) {
        return new ResultVO(OK, "成功", data);
    }

    public static ResultVO putSuccess(String msg, Object data) {
        return new ResultVO(OK, "msg", data);
    }

    public static ResultVO putError() {
        return new ResultVO(ERROR, "失败");
    }


    public static ResultVO putError(Object msg) {
        String strMsg = null;
        if (msg != null) strMsg = msg.toString();
        return new ResultVO(ERROR, strMsg, null);
    }

    public static ResultVO putError(Object msg, Object data) {
        String strMsg = null;
        if (msg != null) strMsg = msg.toString();
        return new ResultVO(ERROR, strMsg, data);
    }

    public static ResultVO put(Integer code, String msg) {
        return new ResultVO(code, msg);
    }

    public static ResultVO put(Integer code, String msg, Object data) {
        return new ResultVO(code, msg, data);
    }
}
