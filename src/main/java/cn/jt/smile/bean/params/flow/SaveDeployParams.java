package cn.jt.smile.bean.params.flow;

import lombok.Data;

/**
 * @author smile
 */
@Data
public class SaveDeployParams {

    /**
     * 前端保存流程参数
     */
    private Object o;

    /**
     * 流程业务编码
     */
    private String businessCode;

    /**
     * FlowAble流程部署成功返回的ID
     */
    private String saveModelId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 启用状态
     */
    private Boolean isEnable;
}
