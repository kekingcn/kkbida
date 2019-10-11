package cn.keking.callcenter.task;

import cn.keking.callcenter.config.Constants;
import cn.keking.callcenter.model.entity.CallBackLogPO;
import cn.keking.callcenter.model.entity.CallBackLogRepository;
import cn.keking.callcenter.model.entity.CallBackTaskPO;
import cn.keking.callcenter.model.entity.CallBackTaskRepository;
import cn.keking.callcenter.sdk.model.CallBackTask;
import com.alibaba.fastjson.JSON;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.klock.annotation.Klock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by kl on 2018/7/20.
 * Content :任务队列服务
 */
@Component
public class TaskService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedissonClient client;

    @Autowired
    private CallBackTaskRepository callBackTaskRepository;

    @Autowired
    private CallBackLogRepository callBackLogRepository;

    private RestTemplate restTemplate = new RestTemplate();

    private static final Long DELAY = 1L;
    private static final TimeUnit DELAY_TIMEUNIT = TimeUnit.MINUTES;

    /**
     * 添加延迟任务
     * @param task
     * @param delay
     * @param timeUnit
     */
    public void addDelayTask(CallBackTask task, Long delay, TimeUnit timeUnit){
        RBlockingQueue<CallBackTask> blockingQueue = client.getBlockingQueue(Constants.REDIS_QUEUE_NAME);
        RDelayedQueue delayedQueue = client.getDelayedQueue(blockingQueue);
        delayedQueue.offer(task,delay,timeUnit);
    }

    /**
     * 添加即时任务
     * @param task
     */
    public void addTask(CallBackTask task){
        task.setTaskId(UUID.randomUUID().toString());
        task.setCreateDate(new Timestamp(System.currentTimeMillis()));
        task.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
        RBlockingQueue<CallBackTask> blockingQueue = client.getBlockingQueue(Constants.REDIS_QUEUE_NAME);
        blockingQueue.addAsync(task);
    }


    /**
     * 移除队列中的某一个任务
     * @param task
     *
    @Klock(keys = {"#task.taskId"})
    public void removeTask(CallBackTask task) {
        RBlockingQueue<CallBackTask> blockingQueue = client.getBlockingQueue(Constants.REDIS_QUEUE_NAME);
        RDelayedQueue delayedQueue = client.getDelayedQueue(blockingQueue);
        Optional<CallBackTask> findTask = ((List<CallBackTask>) delayedQueue.readAll()).stream().filter(t -> t.getTaskId().equals(task.getTaskId())).findFirst();
        if (findTask.isPresent()) {
            delayedQueue.remove(findTask.get());
        }
    } */


    /**
     * 执行任务
     * @param callBackTaskPO
     */
    @Klock(keys = {"#callBackTaskPO.taskId"})
    public boolean execTask(CallBackTaskPO callBackTaskPO) {
        //判断是否成功任务之前是否执行成功，成功则不再执行，直接返回true
        if (callBackTaskPO.getSuccess()) {
            return true;
        }
        String httpResponseStr = "";
        HttpStatus httpStatus = null;
        boolean execResult = false;
        try {
            callBackTaskPO.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
            callBackTaskPO.setCallCount(callBackTaskPO.getCallCount() + 1);
            if (callBackTaskPO.getCallCount() >= callBackTaskPO.getLimitCallCount()) {
                callBackTaskPO.setExpireTimestamp(null);
            }
            switch (callBackTaskPO.getRequestMethod()) {
                case GET: {
                    ResponseEntity<String> responseEntity = restTemplate.getForEntity(callBackTaskPO.getUrl(), String.class, callBackTaskPO.getRequestParam());
                    httpStatus = responseEntity.getStatusCode();
                    httpResponseStr = responseEntity.getBody();
                    break;
                }
                case POST: {
                    ResponseEntity<String> responseEntity = restTemplate.postForEntity(callBackTaskPO.getUrl(), callBackTaskPO.getRequestParam(), String.class);
                    httpStatus = responseEntity.getStatusCode();
                    httpResponseStr = responseEntity.getBody();
                    break;
                }
                default:
                    break;
            }
            if (HttpStatus.OK.equals(httpStatus) && httpResponseStr != null && httpResponseStr.contains(callBackTaskPO.getExpectResult())) {
                execResult = true;
            }
        } catch (Exception e) {
            logger.error("回调异常", e);
            httpResponseStr = e.getLocalizedMessage();
        }
        if (httpResponseStr != null && httpResponseStr.length() > Constants.MAX_RESPONSE_LENTHG) {
            logger.warn("响应结果过长，已自动截取");
            httpResponseStr = httpResponseStr.substring(0, Constants.MAX_RESPONSE_LENTHG);
        }
        callBackTaskPO.setSuccess(execResult);
        if(execResult) {
            callBackTaskPO.setExpireTimestamp(null);
        }
        callBackTaskRepository.save(callBackTaskPO);

        CallBackLogPO callBackLogPO = new CallBackLogPO();
        callBackLogPO.setTaskId(callBackTaskPO.getTaskId());
        callBackLogPO.setResponseTxt(httpResponseStr);
        callBackLogPO.setSuccess(execResult);
        callBackLogRepository.save(callBackLogPO);
        logger.info("任务处理完成，结果:{}，重试次数:{}，任务详情:{}，响应结果:{}", execResult,callBackTaskPO.getCallCount(), JSON.toJSON(callBackTaskPO), httpResponseStr);
        return execResult;
    }

    /**
     * 失败的task处理
     * @param callBackTaskPO
     */
    public void execFailureTask(CallBackTaskPO callBackTaskPO) {
        callBackTaskPO.setExpireTimestamp(new Timestamp(System.currentTimeMillis() + DELAY_TIMEUNIT.toMillis(DELAY)));
        CallBackTask task = new CallBackTask();
        BeanUtils.copyProperties(callBackTaskPO, task);
        callBackTaskRepository.save(callBackTaskPO);
        addDelayTask(task, DELAY, DELAY_TIMEUNIT);
    }
}
