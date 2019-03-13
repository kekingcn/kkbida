package cn.keking.callcenter.task;

import cn.keking.callcenter.component.WebHookClientComponent;
import cn.keking.callcenter.config.Constants;
import cn.keking.callcenter.exception.WebHookException;
import cn.keking.callcenter.model.entity.CallBackTaskPO;
import cn.keking.callcenter.model.entity.CallBackTaskRepository;
import cn.keking.callcenter.model.entity.WebHookPO;
import cn.keking.callcenter.sdk.exception.CallBackException;
import cn.keking.callcenter.sdk.model.CallBackTask;
import cn.keking.callcenter.service.ManagerService;
import com.alibaba.fastjson.JSON;
import com.dingtalk.chatbot.SendResult;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kl on 2018/11/27.
 * Content :队列任务处理器
 */
@Component
public class TaskDueueHandle {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedissonClient client;

    @Autowired
    private TaskService taskService;

    @Autowired
    private CallBackTaskRepository callBackTaskRepository;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private WebHookClientComponent webHookClientComponent;

    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    @PostConstruct
    public void start() {
        DueueConsumer consumer = new DueueConsumer(taskService, client);
        executorService.submit(consumer);
        executorService.submit(consumer);
    }

    class DueueConsumer implements Runnable {

        private TaskService taskService;

        private RedissonClient redissonClient;

        public DueueConsumer(TaskService taskService, RedissonClient redissonClient) {
            this.taskService = taskService;
            this.redissonClient = redissonClient;
        }

        @Override
        public void run() {
            RBlockingQueue<CallBackTask> blockingQueue = redissonClient.getBlockingQueue(Constants.REDIS_QUEUE_NAME);
            while (true && !redissonClient.isShutdown() && !redissonClient.isShuttingDown()) {
                CallBackTask task = null;
                try {
                    task = blockingQueue.take();
                    CallBackTaskPO callBackTaskPO = new CallBackTaskPO();
                    Optional<CallBackTaskPO> optionalCallBackTaskPO = callBackTaskRepository.findById(task.getTaskId());
                    if (optionalCallBackTaskPO.isPresent()) {
                        callBackTaskPO = optionalCallBackTaskPO.get();
                    } else {
                        BeanUtils.copyProperties(task, callBackTaskPO);
                    }
                    if (callBackTaskPO.getCallCount() < callBackTaskPO.getLimitCallCount()) {
                        boolean execReulst = taskService.execTask(callBackTaskPO);
                        if (!execReulst) {
                            if (callBackTaskPO.getCallCount() < callBackTaskPO.getLimitCallCount()) {
                                taskService.execFailureTask(callBackTaskPO);
                            }
                            if (callBackTaskPO.getCallCount() >= callBackTaskPO.getLimitCallCount()) {
                                List<WebHookPO> webHookList = managerService.getWebHookList();
                                String content = "";
                                for (WebHookPO webHook : webHookList) {
                                    try {
                                        content = webHook.getContent();
                                        content = content.replace("${taskId}", task.getTaskId());
                                        content = content.replace("${url}", task.getUrl());
                                        content = content.replace("${createTime}", task.getCreateDate().toString());
                                        content = content.replace("${lastModifiedDate}", task.getLastModifiedDate().toString());
                                        content = content.replace("${sourceAppName}", task.getSourceAppName() == null ? "" : task.getSourceAppName());
                                        content = content.replace("${targetAppName}", task.getTargetAppName() == null ? "" : task.getTargetAppName());
                                        content = content.replace("${callCount}", String.valueOf(task.getCallCount()));
                                        content = content.replace("${limitCallCount}", String.valueOf(task.getLimitCallCount()));
                                        SendResult sendResult = webHookClientComponent.send(webHook, content);
                                        if (!sendResult.isSuccess()) {
                                            throw new WebHookException("异常信息为：" + content);
                                        } else {
                                            logger.info("发送webhook通知成功，webhook元信息：{}, msg：{}", webHook, content);
                                        }
                                    } catch (Exception e) {
                                        logger.error("webhook通知异常，webhook元信息：{}, msg：{}", webHook, content);
                                    }
                                }
                            }
                            throw new CallBackException();
                        }
                    }
                } catch (Exception e) {
                    if (!(e instanceof CallBackException)) {
                        logger.error("任务处理异常："+ JSON.toJSONString(task), e);
                    }
                }
            }
        }
    }
}
