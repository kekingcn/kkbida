package cn.keking.callcenter.component;

import cn.keking.callcenter.exception.WebHookException;
import cn.keking.callcenter.model.entity.WebHookPO;
import cn.keking.callcenter.sdk.model.enums.RequestMethodEnum;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.chatbot.SendResult;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @auther: chenjh
 * @time: 2019/3/6 14:44
 * @description
 */
@Component
public class WebHookClientComponent {

    private HttpClient httpclient = HttpClients.createDefault();

    private static final String DINGDING_WEBHOOK_URL_PREFIX = "https://oapi.dingtalk.com";

    public SendResult send(WebHookPO webhook, String msgContent) throws IOException, WebHookException {
        if (webhook == null || msgContent.trim().isEmpty()) {
            throw new WebHookException("webhook对象或消息对象为空");
        }
        if (!RequestMethodEnum.POST.equals(webhook.getRequestMethod())) {
            throw new WebHookException("暂只支持POST请求");
        }
        HttpPost httppost = new HttpPost(webhook.getUrl());
        httppost.addHeader("Content-Type", webhook.getContentType());
        StringEntity se = new StringEntity(msgContent, "utf-8");
        httppost.setEntity(se);
        SendResult sendResult = new SendResult();
        HttpResponse response = httpclient.execute(httppost);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            //钉钉消息，判断返回码
            if (webhook.getUrl().toLowerCase().startsWith(DINGDING_WEBHOOK_URL_PREFIX)) {
                String result = EntityUtils.toString(response.getEntity());
                JSONObject obj = JSONObject.parseObject(result);
                Integer errcode = obj.getInteger("errcode");
                sendResult.setErrorCode(errcode);
                sendResult.setErrorMsg(obj.getString("errmsg"));
                sendResult.setIsSuccess(errcode.equals(0));
            } else {
                sendResult.setIsSuccess(true);
            }
        } else {
            sendResult.setIsSuccess(false);
        }
        return sendResult;
    }
}
