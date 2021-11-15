package cn.jt.smile.bean.params.flow;

import lombok.Data;

/**
 * @author smile
 */
@Data
public class RuleNodeParams {
    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则类型
     */
    private Integer ruleType;

    private String key;

    private String condition;

    private String value;
}
