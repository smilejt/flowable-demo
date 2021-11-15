package cn.jt.smile.bean.vo;

import cn.jt.smile.bean.po.CustomizeCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author smile
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomizeConditionVO extends CustomizeCondition {

    private static final long serialVersionUID = -8694716737268352838L;
    /**
     * 字典选项列表
     */
    private Object dicts;
}
