package cn.jt.smile.bean.params.instance;

import lombok.Data;

/**
 * @author smile
 */
@Data
public class AddTaskUserParams {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 流程实例ID
     */
    private String instanceId;

    /**
     * 流程节点ID
     */
    private String flowNodeId;
}
