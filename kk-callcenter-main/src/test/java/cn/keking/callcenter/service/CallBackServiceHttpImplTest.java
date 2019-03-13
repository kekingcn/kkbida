package cn.keking.callcenter.service;

import cn.keking.callcenter.sdk.model.CallBackTask;
import cn.keking.callcenter.sdk.model.enums.RequestMethodEnum;
import cn.keking.callcenter.sdk.service.CallBackServiceHttpImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther: chenjh
 * @time: 2018/12/6 11:50
 * @description
 */
public class CallBackServiceHttpImplTest {

    @Test
    public void call() {
        String url = "http://127.0.0.1:8080";
        CallBackServiceHttpImpl callBackServiceHttp = new CallBackServiceHttpImpl(url,"admin","admin");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("city", "上海");
        CallBackTask task= new CallBackTask();
        task.setUrl("https://www.apiopen.top/weatherApi");
        task.setSourceAppName("callBack");
        task.setTargetAppName("callBack");
        task.setRequestParam(paramMap);
        task.setRequestMethod(RequestMethodEnum.GET);
        task.setLimitCallCount(3);
        task.setExpectResult("code: 200");
        String result = callBackServiceHttp.call(task);
        Assert.assertFalse(result.contains("任务添加失败"));
    }
}