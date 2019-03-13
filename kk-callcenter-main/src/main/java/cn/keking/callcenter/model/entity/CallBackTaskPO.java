package cn.keking.callcenter.model.entity;

import cn.keking.callcenter.sdk.model.enums.RequestMethodEnum;
import com.alibaba.fastjson.JSON;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

/**
 * Created by kl on 2018/11/27.
 * Content :回调任务实体
 */
@Entity
@Table(name = "callback_task", indexes = {@Index(name = "idx_callback_task_task_id", columnList = "taskId")})
public class CallBackTaskPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false)
    private String taskId;

    private String url;

    @Enumerated(EnumType.STRING)
    private RequestMethodEnum requestMethod;

    @Transient
    private Map<String, Object> requestParam;

    @Column(columnDefinition = "TEXT")
    private String requestParamTxt;

    private String sourceAppName;

    private String targetAppName;

    private String sourceIp;

    private String expectResult;

    private int callCount;

    private int limitCallCount;

    private Boolean isSuccess = false;

    private Timestamp createDate;

    private Timestamp lastModifiedDate;

    private Timestamp expireTimestamp;

    @Transient
    private String expireTime;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId (String taskId) {
        this.taskId = taskId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RequestMethodEnum getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethodEnum requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Map<String, Object> getRequestParam() {
        return JSON.parseObject(requestParamTxt, Map.class);
    }

    public void setRequestParam(Map<String, Object> requestParam) {
        this.requestParam = requestParam;
        this.requestParamTxt = JSON.toJSONString(requestParam);
    }

    public String getRequestParamTxt() {
        return requestParamTxt;
    }

    public void setRequestParamTxt(String requestParamTxt) {
        this.requestParamTxt = requestParamTxt;
    }

    public String getSourceAppName() {
        return sourceAppName;
    }

    public void setSourceAppName(String sourceAppName) {
        this.sourceAppName = sourceAppName;
    }

    public String getTargetAppName() {
        return targetAppName;
    }

    public void setTargetAppName(String targetAppName) {
        this.targetAppName = targetAppName;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getExpectResult() {
        return expectResult;
    }

    public void setExpectResult(String expectResult) {
        this.expectResult = expectResult;
    }

    public int getCallCount() {
        return callCount;
    }

    public CallBackTaskPO setCallCount(int callCount) {
        this.callCount = callCount;
        return this;
    }

    public int getLimitCallCount() {
        return limitCallCount;
    }

    public CallBackTaskPO setLimitCallCount(int limitCallCount) {
        this.limitCallCount = limitCallCount;
        return this;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Timestamp getExpireTimestamp() {
        return expireTimestamp;
    }

    public void setExpireTimestamp(Timestamp expireTimestamp) {
        this.expireTimestamp = expireTimestamp;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }
}
