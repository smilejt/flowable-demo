package cn.jt.smile.constant;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 流程标识
 *
 * @author smile
 */
public enum WorkflowTypeEnum {
    /**
     * 默认审批流程
     */
    DEFAULT_APPROVE_FLOW(1, Lists.newArrayList("startNode", "endNode")),
    /**
     * 默认工单流程
     */
    DEFAULT_WORKFLOW(2, Lists.newArrayList("startNode", "endNode")),
    /**
     * 空
     */
    NULL(null, Lists.newArrayList());

    private final Integer code;
    private final List<String> ids;

    WorkflowTypeEnum(Integer code, List<String> ids) {
        this.code = code;
        this.ids = ids;
    }

    public Integer getCode() {
        return code;
    }

    public List<String> getIds() {
        return ids;
    }

    /**
     * 通过Code获取枚举类型
     *
     * @param code 枚举Code
     * @return 枚举对象
     */
    public static WorkflowTypeEnum get(Integer code) {
        for (WorkflowTypeEnum conditionEnum : WorkflowTypeEnum.values()) {
            if (conditionEnum.code.equals(code)) {
                return conditionEnum;
            }
        }
        return WorkflowTypeEnum.NULL;
    }
}
