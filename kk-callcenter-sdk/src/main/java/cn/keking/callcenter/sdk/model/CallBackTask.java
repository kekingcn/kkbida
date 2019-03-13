package cn.keking.callcenter.sdk.model;


import cn.keking.callcenter.sdk.model.enums.RequestMethodEnum;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

/**
 * @auther: chenjh
 * @time: 2018/11/28 16:06
 * @description
 */
public class CallBackTask implements Serializable {

    private static final long serialVersionUID = 1L;

    private String taskId;

    private String url;

    private RequestMethodEnum requestMethod;

    private Map<String, Object> requestParam;

    private String sourceAppName;

    private String targetAppName;

    private String sourceIp;

    private String expectResult;

    private int callCount;

    private int limitCallCount;

    private Timestamp createDate;

    private Timestamp lastModifiedDate;

    private Timestamp expireTimestamp;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
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
        return requestParam;
    }

    public void setRequestParam(Map<String, Object> requestParam) {
        this.requestParam = requestParam;
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

    public CallBackTask setCallCount(int callCount) {
        this.callCount = callCount;
        return this;
    }

    public int getLimitCallCount() {
        return limitCallCount;
    }

    public CallBackTask setLimitCallCount(int limitCallCount) {
        this.limitCallCount = limitCallCount;
        return this;
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
}
