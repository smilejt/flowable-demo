package cn.jt.smile.constant;


import cn.jt.smile.bean.po.FrontEndFlowComponent;

/**
 * @author smile
 */

public enum InitComponentEnum {
    /**
     * 开始组件
     */
    START_NODE("startNode", new FrontEndFlowComponent(1, "开始", "")),
    /**
     * 结束组件
     */
    END_NODE("endNode", new FrontEndFlowComponent(99, "结束", "")),
    /**
     * 空
     */
    NULL(null, null);

    private final String code;
    private final FrontEndFlowComponent component;

    InitComponentEnum(String code, FrontEndFlowComponent component) {
        this.code = code;
        this.component = component;
    }

    public String getCode() {
        return code;
    }

    public FrontEndFlowComponent getComponent() {
        return component;
    }

    /**
     * 通过Code获取枚举类型
     *
     * @param code 枚举Code
     * @return 枚举对象
     */
    public static InitComponentEnum get(String code) {
        for (InitComponentEnum componentEnum : InitComponentEnum.values()) {
            if (componentEnum.code.equals(code)) {
                return componentEnum;
            }
        }
        return InitComponentEnum.NULL;
    }

    /**
     * 通过Code获取组件对象
     *
     * @param code 枚举的Code
     * @return 描述字符串
     */
    public static FrontEndFlowComponent getDescByCode(String code) {
        InitComponentEnum componentEnum = get(code);
        if (componentEnum == null) {
            return null;
        }
        return componentEnum.getComponent();
    }
}
