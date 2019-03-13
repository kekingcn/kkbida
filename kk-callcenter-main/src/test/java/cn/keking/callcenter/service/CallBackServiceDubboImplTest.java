package cn.keking.callcenter.service;

import cn.keking.callcenter.CallcenterApplicationTests;
import cn.keking.callcenter.sdk.model.CallBackTask;
import cn.keking.callcenter.sdk.service.CallBackService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther: chenjh
 * @time: 2018/12/6 15:41
 * @description
 */
public class CallBackServiceDubboImplTest extends CallcenterApplicationTests {

    @Reference(version = "1.0")
    private CallBackService callBackService;

    @Test
    public void call() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("city", "上海");
        CallBackTask task= new CallBackTask();
        task.setUrl("https://www.apiopen.top/weatherApi");
        task.setSourceAppName("callBack");
        task.setTargetAppName("callBack");
        task.setRequestParam(paramMap);
        task.setLimitCallCount(3);
        task.setExpectResult("code: 200");
        String result = callBackService.call(task);
        Assert.assertFalse(result.contains("任务添加失败"));
    }
}