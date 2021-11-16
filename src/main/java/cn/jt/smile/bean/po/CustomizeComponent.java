package cn.jt.smile.bean.po;

import cn.jt.smile.bean.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 前端组件展示比表
 *
 * @author smile
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("customize_component")
public class CustomizeComponent extends BaseEntity {

    private static final long serialVersionUID = 5171878550954650601L;
    /**
     * 数据业务ID
     */
    private String componentId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 流程标识(1-审批流，2-工单流)
     */
    private Integer workflowType;

    /**
     * 模块组件类型
     */
    private Integer moduleType;

    /**
     * 组件名称
     */
    private String componentName;

    /**
     * 前端组件标识
     */
    private String webComponent;

    /**
     * 逻辑删除标识(0-否，1-是)
     */
    private Boolean delFlag;
}