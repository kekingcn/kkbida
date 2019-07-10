package cn.keking.callcenter.service;

import cn.keking.callcenter.component.WebHookClientComponent;
import cn.keking.callcenter.exception.WebHookException;
import cn.keking.callcenter.model.entity.*;
import cn.keking.callcenter.task.TaskService;
import cn.keking.callcenter.utils.PageUtil;
import com.dingtalk.chatbot.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * @auther: chenjh
 * @time: 2018/12/3 15:38
 * @description
 */
@Service
public class ManagerService {

    Logger logger = LoggerFactory.getLogger(getClass());

    ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Autowired
    private QueryComponet queryComponet;

    @Autowired
    private CallBackLogRepository callBackLogRepository;

    @Autowired
    private CallBackTaskRepository callBackTaskRepository;

    @Autowired
    private WebHookRepository webHookRepository;


    @Autowired
    private WebHookClientComponent webHookClientComponent;

    @Autowired
    private TaskService taskService;

    public PageUtil<CallBackTaskPO> getFullTask(Map<String, String> map) {
        return queryComponet.getFullTask(map);
    }

    public PageUtil<CallBackTaskPO> getFutureTasks(Map<String, String> map) {
        return queryComponet.getFutrueTask(map);
    }

    public int getTotalCount(Map<String, String> map) {
        if (map.get("taskId") == null) {
            return callBackLogRepository.getTotalCount();
        } else {
            return callBackLogRepository.getTotalCount(map.get("taskId"));
        }
    }

    public int getSuccessCount(Map<String, String> map) {
        if (map.get("taskId") == null) {
            return callBackLogRepository.getSuccessCount();
        } else {
            return callBackLogRepository.getSuccessCount(map.get("taskId"));
        }
    }

    public int getFailedCount(Map<String, String> map) {
        if (map.get("taskId") == null) {
            return callBackLogRepository.getFailedCount();
        } else {
            return callBackLogRepository.getFailedCount(map.get("taskId"));
        }
    }

    public String retry(String taskId) {
        Optional<CallBackTaskPO> callBackTask = callBackTaskRepository.findById(taskId);
        if (!callBackTask.isPresent()) {
            return "任务不存在！";
        }
        CallBackTaskPO callBackTaskPO = callBackTask.get();
        boolean result = taskService.execTask(callBackTaskPO);
        return result ? "执行成功" : "执行失败";
    }

    public Map<String, Integer> getCount() {
        Map<String, Integer> resultMap = new HashMap<>();
        final CountDownLatch countDownLatch = new CountDownLatch(3);
        int totalCount = 0;
        int successCount = 0;
        int failedCount = 0;
        Future<Integer> totalFuture = executorService.submit(new GetTotalCountThread(countDownLatch));
        Future<Integer> successFuture = executorService.submit(new GetSuccessCountThread(countDownLatch));
        Future<Integer> failedFutrue = executorService.submit(new GetFailedCountThread(countDownLatch));
        try {
            countDownLatch.await(10, TimeUnit.SECONDS);
            totalCount = totalFuture.get();
            successCount = successFuture.get();
            failedCount = failedFutrue.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("获取统计数据异常", e);
        }
        resultMap.put("totalCount", totalCount);
        resultMap.put("successCount", successCount);
        resultMap.put("failedCount", failedCount);
        return resultMap;
    }

    public List<WebHookPO> getWebHookList() {
        List<WebHookPO> list = new ArrayList<>();
        list = webHookRepository.findAll();
        return list;
    }

    public PageUtil<WebHookPO> queryForWebHookList(Map<String, String> map) {
        return queryComponet.getWebHookList(map);
    }

    public WebHookPO getWebHookById(Integer id) {
        return webHookRepository.getOne(id);
    }

    public WebHookPO saveWebHook(WebHookPO webHookPO) {
        return webHookRepository.save(webHookPO);
    }

    public boolean testWebHook(Integer id) {
        WebHookPO webHookPO = getWebHookById(id);
        String content = webHookPO.getContent();
        content = content.replace("${taskId}", "测试");
        content = content.replace("${url}", "测试");
        content = content.replace("${createTime}", "测试");
        content = content.replace("${lastModifiedDate}", "测试");
        content = content.replace("${sourceAppName}", "测试");
        content = content.replace("${targetAppName}", "测试");
        content = content.replace("${callCount}", "测试");
        content = content.replace("${limitCallCount}", "测试");
        SendResult sendResult = new SendResult();
        sendResult.setIsSuccess(false);
        try {
            sendResult = webHookClientComponent.send(webHookPO, content);
            if (!sendResult.isSuccess()) {
                throw new WebHookException("异常信息为：" + content);
            } else {
                logger.info("发送webhook通知成功，webhook元信息：{}, msg：{}", webHookPO, content);
            }
        } catch (IOException e) {
            logger.error("webhook通知异常，webhook元信息：{}, msg：{}", webHookPO, content);
        }
        return sendResult.isSuccess() ? true : false;
    }

    public void deleteWebHookById(Integer id) {
        webHookRepository.deleteById(id);
    }

    public Map<String, Map<String, Object>> getCountForChart() {
        Map<String, Map<String, Object>> resultMap = new HashMap<>();
        List<Map<String, Object>> list = callBackLogRepository.getCountForChart();
        list.forEach(item -> resultMap.put(item.get("date").toString(), item));
        return resultMap;
    }

    public PageUtil<CallBackLogPO> getTaskLog(Map<String, String> map) {
        return queryComponet.getTaskLog(map);
    }

    class GetTotalCountThread implements Callable<Integer> {
        private CountDownLatch countDownLatch;
        GetTotalCountThread(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }
        @Override
        public Integer call() {
            int count = callBackLogRepository.getTotalCount();
            countDownLatch.countDown();
            return count;
        }
    }

    class GetSuccessCountThread implements Callable<Integer> {
        private CountDownLatch countDownLatch;
        GetSuccessCountThread(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }
        @Override
        public Integer call()  {
            int count = callBackLogRepository.getSuccessCount();
            countDownLatch.countDown();
            return count;
        }
    }

    class GetFailedCountThread implements Callable<Integer> {
        private CountDownLatch countDownLatch;
        GetFailedCountThread(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }
        @Override
        public Integer call() {
            int count = callBackLogRepository.getFailedCount();
            countDownLatch.countDown();
            return count;
        }
    }
}
