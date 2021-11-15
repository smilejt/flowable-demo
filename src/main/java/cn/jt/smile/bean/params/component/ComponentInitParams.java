package cn.jt.smile.bean.params.component;

import lombok.Data;

import java.util.List;

/**
 * @author smile
 */
@Data
public class ComponentInitParams {
    /**
     * 产品ID
     */
    String productId;
    /**
     * 租户ID
     */
    Long tenantId;
    /**
     * 初始化流程列表(1-默认组件审批流程, 2-默认组件工单流)
     */
    List<Integer> initFlows;
}
