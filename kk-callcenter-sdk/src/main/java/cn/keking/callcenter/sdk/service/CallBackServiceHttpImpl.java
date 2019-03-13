package cn.keking.callcenter.sdk.service;

import cn.keking.callcenter.sdk.model.CallBackTask;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by kl on 2018/11/27.
 * Content :http方式实现回调任务的添加
 */
public class CallBackServiceHttpImpl implements CallBackService {

    private String callBackBaseUrl;

    private String username;

    private String password;

   private RestTemplate restTemplate = new RestTemplate();

    public CallBackServiceHttpImpl(String callBackBaseUrl, String username, String password) {
        this.callBackBaseUrl = callBackBaseUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    public String doCall(CallBackTask task) {
        String url = callBackBaseUrl + "/api/addTask";
        HttpHeaders headers = new HttpHeaders();
        headers.add("password", password);
        headers.add("username", username);
        ResponseEntity entity = restTemplate.postForEntity(url, new HttpEntity<>(task, headers), String.class, task);
        return entity.getBody().toString();
    }
}
