package cn.jt.smile.constant;

import lombok.Getter;

/**
 * @author admin
 */
@Getter
public enum FlowActiveEnum {
    /**
     * 未执行
     */
    NOT_EXECUTION(1, "未执行"),
    /**
     * 用户执行
     */
    USER_EXECUTION(2, "用户执行"),
    /**
     * 系统执行
     */
    SYSTEM_EXECUTION(3, "系统执行"),
    /**
     * 系统放弃
     */
    System_give_up(4, "系统放弃"),

    /**
     * 未知状态
     */
    NULL(-1, "未知状态");
    private final Integer code;
    private final String describe;

    FlowActiveEnum(Integer code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    /**
     * 通过Code获取枚举类型
     *
     * @param code 枚举Code
     * @return 枚举对象
     */
    public static FlowActiveEnum get(Integer code) {
        for (FlowActiveEnum activeEnum : FlowActiveEnum.values()) {
            if (activeEnum.code.equals(code)) {
                return activeEnum;
            }
        }
        return NULL;
    }

    /**
     * 通过Code获取枚举描述
     *
     * @param code 枚举的Code
     * @return 描述字符串
     */
    public static String getDescByCode(Integer code) {
        FlowActiveEnum activeEnum = get(code);
        if (activeEnum == null) {
            return null;
        }
        return activeEnum.getDescribe();
    }
}
