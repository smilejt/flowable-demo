package cn.jt.smile.bean.params.component;

import lombok.Data;

/**
 * @author smile
 */
@Data
public class ComponentQueryParams {

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 流程标识
     */
    private Integer workflowType;
}
