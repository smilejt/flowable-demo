package cn.jt.smile.bean.params.approval;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author smile
 */
@Data
public class StartFlowParams {
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 产品ID
     */
    private String productId;
    /**
     * 提交审批数据ID
     */
    @NotBlank(message = "提交审批数据ID不能为空")
    private String dataKey;
    /**
     * 数据权限ID
     */
    @NotBlank(message = "数据权限ID不能为空")
    private String authorityId;
    /**
     * 业务模块编码
     */
    @NotBlank(message = "业务模块编码不能为空")
    private String businessCode;
    /**
     * 流程定义ID
     */
    @NotBlank(message = "流程定义ID不能为空")
    private String deployId;
    /**
     * 流程发起人ID
     */
    @NotBlank(message = "流程发起人ID不能为空")
    private String userId;
    /**
     * 提交审批的数据对象
     */
    @NotBlank(message = "提交审批的数据对象不能为空")
    private Object o;
}
