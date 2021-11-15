package cn.jt.smile.constant;

import lombok.Getter;

/**
 * @author smile
 */

@Getter
public enum AuditStatusEnum {
    /**
     * 草稿
     */
    DRAFT(1, "草稿"),
    /**
     * 审核中
     */
    AUDITING(2, "审核中"),
    /**
     * 审核通过
     */
    PASS(3, "审核通过"),
    /**
     * 审核失败
     */
    FAIL(4, "审核失败"),
    /**
     * 未知状态
     */
    NULL(-1, "未知状态");

    private final Integer code;
    private final String describe;

    AuditStatusEnum(Integer code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    /**
     * 通过Code获取枚举类型
     *
     * @param code 枚举Code
     * @return 枚举对象
     */
    public static AuditStatusEnum get(Integer code) {
        for (AuditStatusEnum statusEnum : AuditStatusEnum.values()) {
            if (statusEnum.code.equals(code)) {
                return statusEnum;
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
    @SuppressWarnings("unused")
    public static String getDescByCode(Integer code) {
        AuditStatusEnum statusEnum = get(code);
        if (statusEnum == null) {
            return null;
        }
        return statusEnum.getDescribe();
    }
}
