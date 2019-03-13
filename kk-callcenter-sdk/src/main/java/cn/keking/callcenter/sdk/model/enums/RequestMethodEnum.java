package cn.keking.callcenter.sdk.model.enums;

/**
 * @auther: chenjh
 * @time: 2018/11/29 9:23
 * @description
 */
public enum RequestMethodEnum {

    GET("GET"),
    POST("POST");

    private String name;

    RequestMethodEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
