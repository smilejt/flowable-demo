package cn.jt.smile.constant;

/**
 * 前端条件符号和flow匹配枚举
 *
 * @author smile
 */
@SuppressWarnings("unused")
public enum ConditionSymbolEnum {
    /**
     * 为空
     */
    NULL("为空", "== null", "%s 为空"),
    /**
     * 不为空
     */
    NOT_NULL("不为空", "!= notnull", "%s 不为空"),
    /**
     * 属于
     */
    BELONG("属于", "%s == %s", "%s 属于 %s"),
    /**
     * 不属于
     */
    UN_BELONG("不属于", "%s != %s", "%s 不属于 %s"),
    /**
     * 等于
     */
    EQUAL("等于", "==", "%s 等于 %s"),
    /**
     * 不等于
     */
    UNEQUAL("不等于", "!=", "%s 不等于 %s"),
    /**
     * 大于
     */
    MORE_THAN("大于", ">", "%s 大于 %s"),
    /**
     * 小于
     */
    LESS_THAN("小于", "<", "%s 小于 %s"),
    /**
     * 大于等于
     */
    MORE_THAN_OR_EQUAL("大于等于", ">=", "%s 大于等于 %s"),
    /**
     * 小于等于
     */
    LESS_THAN_OR_EQUAL("小于等于", "<=", "%s 小于等于 %s"),
    /**
     * 介于
     */
    BETWEEN("介于", null, "%s 介于 %s 之间"),
    /**
     * 未匹配
     */
    NOT_MATCH(null, null, "未匹配到符号 ");

    private final String viewSymbol;
    private final String flowSymbol;
    private final String describe;

    ConditionSymbolEnum(String viewSymbol, String flowSymbol, String describe) {
        this.viewSymbol = viewSymbol;
        this.flowSymbol = flowSymbol;
        this.describe = describe;
    }

    public String getViewSymbol() {
        return viewSymbol;
    }

    public String getDescribe() {
        return describe;
    }

    public String getFlowSymbol() {
        return flowSymbol;
    }

    /**
     * 通过Code获取枚举类型
     *
     * @param code 枚举Code
     * @return 枚举对象
     */
    public static ConditionSymbolEnum get(String code) {
        for (ConditionSymbolEnum symbolEnum : ConditionSymbolEnum.values()) {
            if (symbolEnum.viewSymbol.equals(code)) {
                return symbolEnum;
            }
        }
        return NOT_MATCH;
    }
}
