package cn.jt.smile.service;

/**
 * @author smile
 */
public interface FlowManageService {

    /**
     * 流程模版保存
     *
     * @param o 前端提交参数
     * @return 保存结果
     */
    boolean saveModel(Object o);

    /**
     * 部署流程定义
     *
     * @param deployId 系统流程定义ID
     * @return 部署结果
     */
    boolean deployFlow(String deployId);

    /**
     * 检查用户是否存在流程配置或审批任务
     * @param userId 用户ID
     * @return 检查结果
     */
    boolean checkUserInFlowOrTask(String userId);
}
