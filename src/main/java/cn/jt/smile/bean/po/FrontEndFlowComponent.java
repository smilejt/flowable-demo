package cn.jt.smile.bean.po;

import lombok.Data;

/**
 * 前端组件对象
 *
 * @author smile
 */
@Data
public class FrontEndFlowComponent {

    /**
     * 模块组件类型
     */
    private Integer moduleType;

    /**
     * 模块组件名称
     */
    private String componentName;

    /**
     * 前端组件标识
     */
    private String webComponent;

    public FrontEndFlowComponent(Integer moduleType, String componentName, String webComponent) {
        this.moduleType = moduleType;
        this.componentName = componentName;
        this.webComponent = webComponent;
    }
}
