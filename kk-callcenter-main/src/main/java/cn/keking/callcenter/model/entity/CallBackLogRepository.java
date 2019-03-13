package cn.keking.callcenter.model.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @auther: chenjh
 * @time: 2018/11/30 10:17
 * @description
 */
@Repository
public interface CallBackLogRepository extends JpaRepository<CallBackLogPO, Long> {
    @Query(value = "select count(1) from call_back_log", nativeQuery = true)
    int getTotalCount();

    @Query(value = "select count(1) from call_back_log where taskId = :taskId", nativeQuery = true)
    int getTotalCount(@Param(value = "taskId") String taskId);

    @Query(value = "select count(1) from call_back_log where isSuccess = 1", nativeQuery = true)
    int getSuccessCount();

    @Query(value = "select count(1) from call_back_log where isSuccess = 1 and taskId = :taskId", nativeQuery = true)
    int getSuccessCount(@Param(value = "taskId") String taskId);

    @Query(value = "select count(1) from call_back_log where isSuccess = 0", nativeQuery = true)
    int getFailedCount();

    @Query(value = "select count(1) from call_back_log where isSuccess = 0 and taskId = :taskId", nativeQuery = true)
    int getFailedCount(@Param(value = "taskId") String taskId);

    @Query(value = "SELECT t.date, t.totalCount, t.successCount, t.totalCount - t.successCount AS failedCount FROM ( SELECT x.*, IFNULL(y.successCount, 0) AS successCount FROM ( SELECT b.date, COUNT(1) AS totalCount FROM ( SELECT DATE_FORMAT(a.createDate, '%Y-%m-%d') AS date, a.isSuccess FROM call_back_log a WHERE DATEDIFF(CURDATE(), a.createDate) < 7 ) b GROUP BY b.date ) x LEFT JOIN ( SELECT b.date, COUNT(1) AS successCount FROM ( SELECT DATE_FORMAT(a.createDate, '%Y-%m-%d') AS date, a.isSuccess FROM call_back_log a WHERE DATEDIFF(CURDATE(), a.createDate) < 7 ) b WHERE b.isSuccess = 1 GROUP BY b.date ) y ON x.date = y.date ) t ORDER BY t.date DESC", nativeQuery = true)
    List<Map<String, Object>> getCountForChart();
}
