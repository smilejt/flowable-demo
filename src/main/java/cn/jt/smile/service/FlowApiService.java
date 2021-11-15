package cn.jt.smile.service;

import cn.jt.smile.bean.params.approval.CompleteTaskParams;
import cn.jt.smile.bean.params.approval.SaveFlowParams;
import cn.jt.smile.bean.params.approval.StartFlowParams;
import cn.jt.smile.bean.vo.RunFlowVO;

import java.util.Map;

/**
 * FLowAble 接口，外部不直接调用
 *
 * @author smile
 */
public interface FlowApiService {

    /**
     * 保存流程
     *
     * @param params 保存流程参数
     * @return 流程模版ID
     */
    String saveFlow(SaveFlowParams params);

    /**
     * 启动流程
     *
     * @param params 启动参数集合
     * @return 流程实例ID
     */
    RunFlowVO startFlow(StartFlowParams params);

    /**
     * 执行任务
     *
     * @param params 任务执行参数
     * @return 任务执行结果
     */
    RunFlowVO runTask(CompleteTaskParams params);

    /**
     * 检查流程是否结束
     *
     * @param instanceId 流程实例ID
     * @return 查询结果(true - 结束, false - 未结束)
     */
    boolean checkFlowEnd(String instanceId);

    /**
     * 查询执行中的Task集合
     *
     * @param instanceId 流程实例ID
     * @return 查询结果集 Map<用户ID, TaskId></>
     */
    Map<String, String> getActiveTask(String instanceId);
}
