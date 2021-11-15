package cn.jt.smile.bean.po;

import cn.jt.smile.bean.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 审批角色配置表
 *
 * @author smile
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("approve_role")
public class ApproveRole extends BaseEntity {

    private static final long serialVersionUID = -4404975772045987241L;
    /**
     * 业务数据ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String roleId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 审批角色名称
     */
    private String roleName;

    /**
     * 账号数量
     */
    private Integer accountNumber;

    /**
     * 账号名称(逗号分割)
     */
    @TableField(fill = FieldFill.UPDATE)
    private String accountName;

    /**
     * 应用的审批流数据
     */
    private Integer approveNumber;

    /**
     * 审批流名称(逗号分割)
     */
    @TableField(fill = FieldFill.UPDATE)
    private String approveName;

    /**
     * 临时(0-否，1-是)
     */
    private Boolean temporary;

    /**
     * 备注
     */
    private String remark;

    /**
     * 逻辑删除标识(0-否，1-是)
     */
    private Boolean delFlag;
}