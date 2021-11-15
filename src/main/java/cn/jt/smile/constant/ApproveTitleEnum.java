package cn.jt.smile.constant;

/**
 * @author smile
 */
public enum ApproveTitleEnum {
    /**
     * 审批结果-必填
     */
    REQUIRED(1, "必填"),
    /**
     * 审批结果-不填
     */
    NOT_NEEDED(2, "不填"),
    /**
     * 审批结果-选填
     */
    OPTIONAL(3, "选填");

    ApproveTitleEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    private final Integer code;
    private final String value;

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static ApproveTitleEnum get(Integer code) {
        for (ApproveTitleEnum approveTitleEnum : ApproveTitleEnum.values()) {
            if (approveTitleEnum.code.equals(code)) {
                return approveTitleEnum;
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    public static String getDescByCode(Integer code) {
        ApproveTitleEnum approveTitleEnum = get(code);
        if (approveTitleEnum == null) {
            return null;
        }
        return approveTitleEnum.getValue();
    }
}
