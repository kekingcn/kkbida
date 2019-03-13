package cn.keking.callcenter.utils;

import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

/**
 * @auther: chenjh
 * @time: 2018/12/3 18:23
 * @description
 */
public class PageUtil<T> {

    /**当前页*/
    private int currentPage;

    /**每页大小*/
    private int rows;
    /**总页数*/
    private int countPage;
    /**总记录数*/
    private long count;
    /**启始记录*/
    private List<T> resultList;

    private int  firstResult;
    public static Map resultSuccess= ImmutableMap.of("result","success");
    public static Map resultFailure= ImmutableMap.of("result","failure");
    public static Map addResultSuccess(Object value){
        return  ImmutableMap.of("result","success","message",value);
    }
    public static Map addResultFailure(Object value){
        if(value==null) {
            value="系统出错了！";
        }
        return ImmutableMap.of("result","failure","message",value);
    }

    public PageUtil() {}

    public PageUtil(int currentPage, int rows) {
        this.currentPage = currentPage;
        this.rows = rows;
        this.firstResult=(currentPage-1)*rows;
    }
    public void setCount(long count) {
        this.count = count;
        if(count%rows!=0){
            this.countPage= (int) ((count/rows)+1);
        }else {
            this.countPage= (int) (count/rows);
        }
    }

    public PageUtil setResultList(List<T> resultList) {
        this.resultList = resultList;
        return this;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCountPage() {
        return countPage;
    }

    public void setCountPage(int countPage) {
        this.countPage = countPage;
    }

    public long getCount() {
        return count;
    }

    public List<T> getResultList() {
        return resultList;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public static Map getResultSuccess() {
        return resultSuccess;
    }

    public static void setResultSuccess(Map resultSuccess) {
        PageUtil.resultSuccess = resultSuccess;
    }

    public static Map getResultFailure() {
        return resultFailure;
    }

    public static void setResultFailure(Map resultFailure) {
        PageUtil.resultFailure = resultFailure;
    }
}
