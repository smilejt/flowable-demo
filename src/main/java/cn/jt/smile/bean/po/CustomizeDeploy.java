package cn.jt.smile.bean.po;

import cn.jt.smile.bean.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程部署表
 *
 * @author smile
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("customize_deploy")
public class CustomizeDeploy extends BaseEntity {

    private static final long serialVersionUID = 8639528633839354285L;
    /**
     * 数据业务ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String deployId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 业务模块编码
     */
    private String businessCode;

    /**
     * 业务流程ID(对应业务动作为保存)
     */
    private String saveModelId;

    /**
     * 流程定义ID
     */
    private String saveDefId;

    /**
     * 流程版本
     */
    private Integer saveVersion;

    /**
     * 前端流程节点JSON
     */
    private String saveNodeData;

    /**
     * 业务部署ID(对应业务系统部署)
     */
    private String deployModelId;

    /**
     * 流程定义ID
     */
    private String deployDefId;

    /**
     * 业务部署版本
     */
    private Integer deployVersion;

    /**
     * 前端流程节点JSON
     */
    private String deployNodeData;

    /**
     * 备注
     */
    private String remark;

    /**
     * 部署状态
     */
    private Boolean deployStatus;

    /**
     * 启用状态
     */
    private Boolean isEnable;

    /**
     * 逻辑删除标识(0-否，1-是)
     */
    private Boolean delFlag;
}