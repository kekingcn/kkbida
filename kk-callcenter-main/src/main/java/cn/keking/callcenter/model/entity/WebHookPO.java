package cn.keking.callcenter.model.entity;

import cn.keking.callcenter.sdk.model.enums.RequestMethodEnum;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @auther: chenjh
 * @time: 2019/3/4 9:29
 * @description
 */
@Entity
@Table(name = "web_hook")
public class WebHookPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private RequestMethodEnum requestMethod = RequestMethodEnum.POST;

    private String url;

    private String contentType = "application/json; charset=utf-8";

    private String memo;

    @Column(columnDefinition = "TEXT")
    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RequestMethodEnum getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethodEnum requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "WebHookPO{" +
                "id=" + id +
                ", requestMethod=" + requestMethod +
                ", url='" + url + '\'' +
                ", contentType='" + contentType + '\'' +
                ", memo='" + memo + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
