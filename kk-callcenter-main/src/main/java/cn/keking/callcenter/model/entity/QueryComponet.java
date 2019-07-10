package cn.keking.callcenter.model.entity;

import cn.keking.callcenter.utils.PageUtil;
import com.querydsl.jpa.JPAQueryBase;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @auther: chenjh
 * @time: 2018/12/3 17:38
 * @description
 */
@Repository
public class QueryComponet {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private EntityManager entityManager;

    private QCallBackTaskPO qCallBackTaskPO = QCallBackTaskPO.callBackTaskPO;

    private QWebHookPO qWebHookPO = QWebHookPO.webHookPO;

    private QCallBackLogPO qCallBackLogPO = QCallBackLogPO.callBackLogPO;

    public PageUtil<CallBackTaskPO> getFullTask(Map<String, String> map) {
        int currentPage = 1;
        int rows = 10;
        try {
            if (map.get("page") != null) {
                currentPage = Integer.parseInt(map.get("page"));
            }
            if (map.get("limit") != null) {
                rows = Integer.parseInt(map.get("limit"));
            }
        } catch (Exception e) {}

        Timestamp startTime = null;
        Timestamp endTime = null;

        try {
            if (map.get("createTime").contains(" - ")) {
                String[] dateAraay = map.get("createTime").split(" - ");
                startTime = new Timestamp(SDF.parse(dateAraay[0]).getTime());
                endTime = new Timestamp(SDF.parse(dateAraay[1]).getTime());
            }
        } catch (Exception e) {}


        JPAQueryBase queryBase = new JPAQuery<CallBackTaskPO>(entityManager).from(qCallBackTaskPO);
        if (map.get("url") != null && StringUtils.isNotBlank(map.get("url"))) {
            queryBase.where(qCallBackTaskPO.url.contains(map.get("url")));
        }
        if (map.get("taskId") != null && StringUtils.isNotBlank(map.get("taskId"))) {
            queryBase.where(qCallBackTaskPO.taskId.eq(map.get("taskId")));
        }
        if ((startTime != null) && (endTime != null)) {
            queryBase.where(qCallBackTaskPO.createDate.between(startTime, endTime));
        }
        queryBase.orderBy(qCallBackTaskPO.isSuccess.asc());
        queryBase.orderBy(qCallBackTaskPO.createDate.desc());
        long count = queryBase.fetchCount();
        PageUtil<CallBackTaskPO> page = new PageUtil<>(currentPage, rows);
        page.setCount(count);
        queryBase.limit(page.getRows()).offset((page.getCurrentPage() - 1) * page.getRows());
        List<CallBackTaskPO> fullTaskList = queryBase.fetch();
        page.setResultList(fullTaskList == null ? new ArrayList<>() : fullTaskList);
        return page;
    }

    public PageUtil<CallBackTaskPO> getFutrueTask(Map<String, String> map) {
        int currentPage = 1;
        int rows = 10;
        try {
            if (map.get("page") != null) {
                currentPage = Integer.parseInt(map.get("page"));
            }
            if (map.get("limit") != null) {
                rows = Integer.parseInt(map.get("limit"));
            }
        } catch (Exception e) {}

        JPAQueryBase queryBase = new JPAQuery<CallBackTaskPO>(entityManager).from(qCallBackTaskPO);
        if (map.get("url") != null && StringUtils.isNotBlank(map.get("url"))) {
            queryBase.where(qCallBackTaskPO.url.contains(map.get("url")));
        }
        if (map.get("taskId") != null && StringUtils.isNotBlank(map.get("taskId"))) {
            queryBase.where(qCallBackTaskPO.taskId.eq(map.get("taskId")));
        }
        queryBase.where(qCallBackTaskPO.expireTimestamp.isNotNull());
        queryBase.orderBy(qCallBackTaskPO.createDate.desc());
        long count = queryBase.fetchCount();
        PageUtil<CallBackTaskPO> page = new PageUtil<>(currentPage, rows);
        page.setCount(count);
        queryBase.limit(page.getRows()).offset((page.getCurrentPage() - 1) * page.getRows());
        List<CallBackTaskPO> list = queryBase.fetch();
        long currentTimeMillis = System.currentTimeMillis();
        list.stream().forEach(callBackTaskPO -> callBackTaskPO.setExpireTime(getExpireTime(callBackTaskPO.getExpireTimestamp(), currentTimeMillis)));
        page.setResultList(list == null ? new ArrayList<>() : list);
        return page;
    }

    public PageUtil<WebHookPO> getWebHookList(Map<String, String> map) {
        int currentPage = 1;
        int rows = 10;
        try {
            if (map.get("page") != null) {
                currentPage = Integer.parseInt(map.get("page"));
            }
            if (map.get("limit") != null) {
                rows = Integer.parseInt(map.get("limit"));
            }
        } catch (Exception e) {}

        JPAQueryBase queryBase = new JPAQuery<CallBackTaskPO>(entityManager).from(qWebHookPO);
        if (map.get("memo") != null) {
            queryBase.where(qWebHookPO.memo.contains(map.get("memo")));
        }
        queryBase.orderBy(qWebHookPO.id.asc());
        long count = queryBase.fetchCount();
        PageUtil<WebHookPO> page = new PageUtil<>(currentPage, rows);
        page.setCount(count);
        queryBase.limit(page.getRows()).offset((page.getCurrentPage() - 1) * page.getRows());
        List<WebHookPO> list = queryBase.fetch();
        page.setResultList(list == null ? new ArrayList<>() : list);
        return page;
    }

    private String getExpireTime(Timestamp expireTime, long currentTimeMillis) {
        long remainSeconds = 0L;
        try {
            long expireTimeMillis = expireTime.getTime();
            remainSeconds = (expireTimeMillis - currentTimeMillis) / 1000;
        } catch (Exception e) {}
        return remainSeconds + "秒";
    }

    public PageUtil<CallBackLogPO> getTaskLog(Map<String, String> map) {
        int currentPage = 1;
        int rows = 10;
        try {
            if (map.get("page") != null) {
                currentPage = Integer.parseInt(map.get("page"));
            }
            if (map.get("limit") != null) {
                rows = Integer.parseInt(map.get("limit"));
            }
        } catch (Exception e) {}

        JPAQueryBase queryBase = new JPAQuery<CallBackTaskPO>(entityManager).from(qCallBackLogPO);
        if (map.get("taskId") != null && StringUtils.isNotBlank(map.get("taskId"))) {
            queryBase.where(qCallBackLogPO.taskId.eq(map.get("taskId")));
        }
        queryBase.orderBy(qCallBackLogPO.createDate.desc());
        long count = queryBase.fetchCount();
        PageUtil<CallBackLogPO> page = new PageUtil<>(currentPage, rows);
        page.setCount(count);
        queryBase.limit(page.getRows()).offset((page.getCurrentPage() - 1) * page.getRows());
        List<CallBackLogPO> list = queryBase.fetch();
        page.setResultList(list == null ? new ArrayList<>() : list);
        return page;
    }
}
