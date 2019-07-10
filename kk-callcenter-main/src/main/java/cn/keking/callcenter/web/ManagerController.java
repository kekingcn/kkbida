package cn.keking.callcenter.web;

import cn.keking.callcenter.model.ResultVO;
import cn.keking.callcenter.model.entity.CallBackLogPO;
import cn.keking.callcenter.model.entity.CallBackTaskPO;
import cn.keking.callcenter.model.entity.WebHookPO;
import cn.keking.callcenter.service.ManagerService;
import cn.keking.callcenter.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

import static cn.keking.callcenter.config.Constants.REDIRECT_VIEW_INDEX;
import static cn.keking.callcenter.config.Constants.REDIRECT_VIEW_LOGIN;

/**
 * Created by kl on 2018/11/26.
 * Content :后台管理
 */
@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Value("${callcenter.system.username}")
    private String username;

    @Value("${callcenter.system.password}")
    private String password;

    @Autowired
    private ManagerService managerService;

    @PostMapping("/login")
    public String login(HttpSession session, String username, String password) {
        if(this.username.equals(username) && this.password.equals(password)){
            session.setAttribute(session.getId(), username);
        }else {
            return REDIRECT_VIEW_LOGIN + "?msg=error";
        }
        return REDIRECT_VIEW_INDEX;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(session.getId());
        return REDIRECT_VIEW_LOGIN;
    }

    @GetMapping("/get")
    @ResponseBody
    public Object get(HttpServletRequest request) {
        return request.getSession().getAttribute("username");
    }

    /**
     * 回调总次数
     * @param map map里带taskId为当前task回调的总次数，不带则为所有的
     * @return
     */
    @PostMapping("/getTotalCount")
    @ResponseBody
    public int getTotalCount(@RequestParam Map<String, String> map) {
        return managerService.getTotalCount(map);
    }

    /**
     * 回调成功次数
     * @param map map里带taskId为当前task回调的回调成功次数，不带则为所有的
     * @return
     */
    @PostMapping("/getSuccessCount")
    @ResponseBody
    public int getSuccessCount(@RequestParam Map<String, String> map) {
        return managerService.getSuccessCount(map);
    }

    /**
     * 回调失败次数
     * @param map map里带taskId为当前task回调的失败次数，不带则为所有的
     * @return
     */
    @PostMapping("/getFailedCount")
    @ResponseBody
    public int getFailedCount(@RequestParam Map<String, String> map) {
        return managerService.getFailedCount(map);
    }

    /**
     * 获取首页数据信息
     * @return
     */
    @GetMapping("/getCount")
    @ResponseBody
    public Map<String, Integer> getCount() {
        return managerService.getCount();
    }

    /**
     * 获取首页图表数据信息
     * @return
     */
    @GetMapping("/getCountForChart")
    @ResponseBody
    public Map<String, Map<String, Object>> getCountForChart() {
        return managerService.getCountForChart();
    }

    /**
     * 所有task查询
     * @param map page 当前页码， limit 每面记录条数   url 回调的url
     * @return
     */
    @PostMapping("/getFullTask")
    @ResponseBody
    public ResultVO getFullTask(@RequestParam Map<String, String> map) {
        PageUtil<CallBackTaskPO> data = managerService.getFullTask(map);
        return ResultVO.putSuccess(data);
    }

    /**
     * 待执行的任务
     * @return
     */
    @PostMapping("/getFutureTasks")
    @ResponseBody
    public ResultVO getFutureTasks(@RequestParam Map<String, String> map) {
        PageUtil<CallBackTaskPO> data = managerService.getFutureTasks(map);
        return ResultVO.putSuccess(data);
    }

    /**
     * 待执行的任务
     * @return
     */
    @PostMapping("/getTaskLogs")
    @ResponseBody
    public ResultVO getTaskLog(@RequestParam Map<String, String> map) {
        PageUtil<CallBackLogPO> data = managerService.getTaskLog(map);
        return ResultVO.putSuccess(data);
    }

    /**
     * 手动重试
     * @return
     */
    @PostMapping("/retry")
    @ResponseBody
    public String retry(@RequestParam(value = "taskId") String taskId) {
        return managerService.retry(taskId);
    }

    /**
     * 获取webhook通知列表
     * @return
     */
    @GetMapping("/getWebHookList")
    @ResponseBody
    public ResultVO getWebHookList(@RequestParam Map<String, String> map) {
        PageUtil<WebHookPO> data = managerService.queryForWebHookList(map);
        return ResultVO.putSuccess(data);
    }

    /**
     * 获取单个webhook通知信息
     * @param id
     * @return
     */
    @GetMapping("/getWebHookById")
    @ResponseBody
    public WebHookPO getWebHookById(@RequestParam(value = "id") Integer id) {
        return managerService.getWebHookById(id);
    }

    /**
     * 删除单个webhook通知信息
     * @param id
     * @return
     */
    @PostMapping("/deleteWebHookById")
    @ResponseBody
    public boolean deleteWebHookById(@RequestParam(value = "id") Integer id) {
        managerService.deleteWebHookById(id);
        return true;
    }

    /**
     * 保存单个webhook通知信息
     * @param webHook
     * @return
     */
    @PostMapping("/saveWebHook")
    @ResponseBody
    public boolean saveWebHook(@RequestBody WebHookPO webHook) {
        WebHookPO webHookPO = managerService.saveWebHook(webHook);
        return webHookPO != null ? true : false;
    }

    /**
     * 测试webhook连通性
     * @param id
     * @return
     */
    @PostMapping("/testWebHook")
    @ResponseBody
    public boolean testWebHook(@RequestParam(value = "id") Integer id) {
        return managerService.testWebHook(id);
    }

}
