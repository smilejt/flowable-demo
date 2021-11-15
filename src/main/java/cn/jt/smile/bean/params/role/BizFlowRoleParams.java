package cn.jt.smile.bean.params.role;

import lombok.Data;

/**
 * @author smile
 */
@Data
public class BizFlowRoleParams {

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 产品ID
     */
    private String productId;
    /**
     * 业务流程ID
     */
    private String bizFlowId;

    /**
     * 审批角色组ID
     */
    private String roleId;

    /**
     * 业务流程名称
     */
    private String bizFlowName;
}
