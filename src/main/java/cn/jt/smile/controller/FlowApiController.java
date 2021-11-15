package cn.jt.smile.controller;

import cn.jt.smile.bean.params.approval.CompleteTaskParams;
import cn.jt.smile.bean.params.approval.SaveFlowParams;
import cn.jt.smile.bean.params.approval.StartFlowParams;
import cn.jt.smile.bean.vo.RunFlowVO;
import cn.jt.smile.service.FlowApiService;
import cn.jt.smile.util.Result;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

/**
 * 内部自测用, 前端忽略
 *
 * @author smile
 */
@RestController
@RequestMapping("/api/flow")
@Slf4j
public class FlowApiController {

    @Resource
    private FlowApiService apiService;

    /**
     * 保存流程实例模版
     *
     * @param params 流程参数
     * @return 流程模版ID
     */
    @PostMapping(value = "saveFlowModel")
    public String saveFlowModel(@RequestBody SaveFlowParams params) {
        log.info("[FlowApiController].[saveFlowModel] ------> Save Flow Model Start, params = {}", JSON.toJSONString(params));
        String modelId = apiService.saveFlow(params);
        log.info("[FlowApiController].[saveFlowModel] ------> Save Flow Model End, modelId = {}", modelId);
        return modelId;
    }

    /**
     * 启动流程实例
     *
     * @param params 启动参数
     * @return 启动结果
     */
    @SuppressWarnings("unchecked")
    @PostMapping(value = "startFlow")
    public Result<RunFlowVO> startFlow(@Valid @RequestBody StartFlowParams params) {
        log.info("[FlowApiController].[startFlow] ------> Run Flow Instance Start, params = {}", JSON.toJSONString(params));
        RunFlowVO result = apiService.startFlow(params);
        log.info("[FlowApiController].[startFlow] ------> Run Flow Instance End, result = {}", JSON.toJSONString(result));
        return Result.success(result);
    }

    @SuppressWarnings("unchecked")
    @PostMapping(value = "runTask")
    public Result<RunFlowVO> runTask(@RequestBody CompleteTaskParams params) {
        log.info("[FlowApiController].[runTask] ------> Run Task Start, params = {}", JSON.toJSONString(params));
        RunFlowVO result = apiService.runTask(params);
        log.info("[FlowApiController].[runTask] ------> Run Task End, result = {}", JSON.toJSONString(result));
        return Result.success(result);
    }

    /**
     * 自测获取活跃Task列表接口
     *
     * @return null
     */
    @SuppressWarnings("unchecked")
    @GetMapping("getActiveTask/{instanceId}")
    public Result<Map<String, String>> getActiveTask(@PathVariable(value = "instanceId") String instanceId) {
        return Result.success(apiService.getActiveTask(instanceId));
    }
}
