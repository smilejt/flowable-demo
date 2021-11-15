package cn.jt.smile.bean.po;

import cn.jt.smile.bean.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 审批角色配置-用户
 *
 * @author smile
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("approve_user")
public class ApproveUser extends BaseEntity {
    private static final long serialVersionUID = 2858366282456826097L;
    /**
     * 业务数据ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String bizId;

    /**
     * 审批角色组ID
     */
    private String roleGroupId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 审批部门ID
     */
    private String approveDepartment;

    /**
     * 逻辑删除标识(0-否，1-是)
     */
    private Boolean delFlag;
}