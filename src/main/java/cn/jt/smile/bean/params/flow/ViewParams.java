package cn.jt.smile.bean.params.flow;

import lombok.Data;

/**
 * @author smile
 */
@Data
public class ViewParams {
    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 流程定义ID
     */
    private String defId;

    /**
     * 前端节点ID
     */
    private String viewNodeId;

    /**
     * flow节点ID
     */
    private String flowNodeId;

    public ViewParams() {
    }

    /**
     * 有参构造方法
     *
     * @param tenantId   租户ID
     * @param productId  产品ID
     * @param viewNodeId 前端节点ID
     * @param flowNodeId flow节点ID
     */
    public ViewParams(Long tenantId, String productId, String viewNodeId, String flowNodeId) {
        this.tenantId = tenantId;
        this.productId = productId;
        this.viewNodeId = viewNodeId;
        this.flowNodeId = flowNodeId;
    }
}
