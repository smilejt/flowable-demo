package cn.jt.smile.bean.vo;

import lombok.Data;

/**
 * @author smile
 */
@Data
public class RunFlowVO {

    /**
     * 流程实例ID
     */
    private String instanceId;

    /**
     * 流程状态
     */
    private Integer state;

    /**
     * 状态文案
     */
    private String stateDescribe;
}
