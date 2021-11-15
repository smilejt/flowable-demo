package cn.jt.smile.constant;

/**
 * 条件类型枚举
 *
 * @author smile
 */
public enum ConditionTypeEnum {
    /**
     * 下拉列表
     */
    DROP_DOWN_LIST(1, "下拉列表"),
    /**
     * 数字输入框
     */
    DIGITAL_INPUT(2, "数字输入"),
    /**
     * 组织选择
     */
    ORGANIZATION_SELECTION(3, "组织选择");

    private final Integer code;
    private final String describe;

    ConditionTypeEnum(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescribe() {
        return describe;
    }

    /**
     * 通过Code获取枚举类型
     *
     * @param code 枚举Code
     * @return 枚举对象
     */
    public static ConditionTypeEnum get(Integer code) {
        for (ConditionTypeEnum conditionEnum : ConditionTypeEnum.values()) {
            if (conditionEnum.code.equals(code)) {
                return conditionEnum;
            }
        }
        return null;
    }

    /**
     * 通过Code获取枚举描述
     *
     * @param code 枚举的Code
     * @return 描述字符串
     */
    public static String getDescByCode(Integer code) {
        ConditionTypeEnum conditionEnum = get(code);
        if (conditionEnum == null) {
            return null;
        }
        return conditionEnum.getDescribe();
    }
}
