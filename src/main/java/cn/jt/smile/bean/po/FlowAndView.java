package cn.jt.smile.bean.po;

import cn.jt.smile.bean.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 前端节点和后端节点关系表
 *
 * @author smile
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("flow_and_view")
public class FlowAndView extends BaseEntity {
    private static final long serialVersionUID = -900369646838662625L;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 流程定义ID
     */
    private String defId;

    /**
     * 前端节点ID
     */
    private String viewNodeId;

    /**
     * flow节点ID
     */
    private String flowNodeId;

    /**
     * 逻辑删除标识(0-否，1-是)
     */
    private Boolean delFlag;

    /**
     * 业务数据ID
     */
    private String bizId;
}