package cn.keking.callcenter.service;

import cn.keking.callcenter.sdk.model.CallBackTask;
import cn.keking.callcenter.sdk.service.CallBackService;
import cn.keking.callcenter.task.TaskService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kl on 2018/11/27.
 * Content : dubbo实现回调任务的添加
 */
@Service(interfaceClass = CallBackService.class,version = "1.0")
public class CallBackServiceDubboImpl implements CallBackService{

    @Autowired
    private TaskService taskService;

    @Override
    public String doCall(CallBackTask task) {
        taskService.addTask(task);
        return task.getTaskId();
    }
}
