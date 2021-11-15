package cn.jt.smile.constant;

import lombok.Getter;

/**
 * @author smile
 */
@Getter
public enum FlowTaskActiveEnum {
    /**
     * 未执行
     */
    NOT_EXECUTION(1, "未执行"),
    /**
     * 用户执行(仅执行任务的用户会出现)
     */
    USER_EXECUTION(2, "用户执行"),
    /**
     * 系统执行(例如:一票通过制的其他用户执行了对应Task)
     */
    SYSTEM_EXECUTION(3, "系统执行"),
    /**
     * 流程被挂起等情况
     */
    SYSTEM_ABANDON(4, "系统执行"),
    /**
     * 未知状态
     */
    NULL(-1, "未知状态");
    private final Integer code;
    private final String describe;

    FlowTaskActiveEnum(Integer code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    /**
     * 通过Code获取枚举类型
     *
     * @param code 枚举Code
     * @return 枚举对象
     */
    public static FlowTaskActiveEnum get(Integer code) {
        for (FlowTaskActiveEnum activeEnum : FlowTaskActiveEnum.values()) {
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
        FlowTaskActiveEnum activeEnum = get(code);
        if (activeEnum == null) {
            return null;
        }
        return activeEnum.getDescribe();
    }
}
