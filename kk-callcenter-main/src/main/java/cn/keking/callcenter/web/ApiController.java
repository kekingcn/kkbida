package cn.keking.callcenter.web;

import cn.keking.callcenter.sdk.model.CallBackTask;
import cn.keking.callcenter.task.TaskService;
import cn.keking.callcenter.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by kl on 2018/11/27.
 * Content :api服务提供
 */
@RequestMapping("/api")
@RestController
public class ApiController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/addTask")
    public String addTask(@RequestBody CallBackTask task, HttpServletRequest request){
        task.setSourceIp(WebUtil.getRequestIp(request));
        taskService.addTask(task);
        return task.getTaskId();
    }
}
