package cn.jt.smile.controller;

import cn.jt.smile.bean.params.flow.ApproveConfigParams;
import cn.jt.smile.service.FlowManageService;
import cn.jt.smile.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 流程管理
 *
 * @author smile
 */
@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/api/flow")
@Slf4j
public class FlowManageController {
    @Resource
    private FlowManageService service;

    /**
     * 保存流程前端接口
     *
     * @param o 前端参数
     * @return 保存结果
     */
    @PostMapping(value = "saveModel")
    public Result<Boolean> saveModel(@RequestBody Object o) {
        log.info("[FlowManageController].[saveModel] ------> User Save Flow Model Start");
        boolean result = service.saveModel(o);
        log.info("[FlowManageController].[saveModel] ------> User Save Flow Model End, result = {}", result);
        if (result) {
            return Result.success(true);
        }
        return Result.fail("操作失败", false);
    }

    /**
     * 部署流程定义
     *
     * @param deployId 系统流程定义ID
     * @return 部署结果
     */
    @GetMapping(value = "deployFlow/{deployId}")
    public Result<Boolean> deployFlow(@PathVariable(value = "deployId") String deployId) {
        log.info("[FlowManageController].[deployFlow] ------> User Deploy Flow Process Start");
        boolean result = service.deployFlow(deployId);
        log.info("[FlowManageController].[deployFlow] ------> User Dave Flow Process End, result = {}", result);
        if (result) {
            return Result.success(true);
        }
        return Result.fail("操作失败", false);
    }

    /**
     * 保存流程前端必传参数
     *
     * @param params 参数对象
     * @return 结果
     */
    @SuppressWarnings("unused")
    @PostMapping(value = "saveModelParams")
    public Result<Boolean> saveModelParams(@RequestBody ApproveConfigParams params) {
        return Result.fail("操作失败", false);
    }

    /**
     * 检查用户是否存在流程配置或审批任务
     *
     * @param userId 用户ID
     * @return 检查结果 true-不存在, 异常-存在
     */
    @GetMapping(value = "checkUser/{userId}")
    public Result<Boolean> checkUserInFlowOrTask(@PathVariable(value = "userId") String userId) {
        log.info("[FlowManageController].[checkUserInFlowOrTask] ------> Check User In Flow Or Task Start, userId = {}", userId);
        boolean result = service.checkUserInFlowOrTask(userId);
        log.info("[FlowManageController].[checkUserInFlowOrTask] ------> Check User In Flow Or Task End, result = {}", result);
        return Result.success(result);
    }
}
