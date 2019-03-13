package cn.keking.callcenter.sdk.service;

import cn.keking.callcenter.sdk.exception.CallBackException;
import cn.keking.callcenter.sdk.model.CallBackTask;

/**
 * Created by kl on 2018/11/27.
 * Content :添加回调任务
 */
public interface CallBackService {

     String PARAM_CHECK_ERROR_PREFIX = "任务添加失败";

     String doCall(CallBackTask task);

     default String call(CallBackTask task) {
          StringBuffer errorMsg = new StringBuffer();
          if (task.getUrl() == null || ((!task.getUrl().toLowerCase().startsWith("http://")) && (!task.getUrl().toLowerCase().startsWith("https://")))) {
               errorMsg.append("，url为空或url格式不正确");
          }
          if (task.getLimitCallCount() <= 0 || task.getLimitCallCount() > 100) {
               errorMsg.append("，最大重试次数不能为空且不能大于100");
          }
          if (task.getRequestMethod() == null) {
               errorMsg.append("，请求方式不能为空");
          }
          if (task.getExpectResult() == null || task.getExpectResult().trim().isEmpty()) {
               errorMsg.append("，期待结果不能为空");
          }
          if (errorMsg.length() > 0) {
               throw new CallBackException(PARAM_CHECK_ERROR_PREFIX + errorMsg.toString());
          } else {
               return this.doCall(task);
          }
     }
}
