package cn.jt.smile.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author smile
 */
@Data
@TableName("biz_flow_role")
public class BizFlowRole implements Serializable {
    private static final long serialVersionUID = -612985757409144710L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 产品ID
     */
    private String productId;
    /**
    * 业务流程ID
    */
    private String bizFlowId;

    /**
    * 审批角色组ID
    */
    private String roleId;

    /**
    * 业务流程名称
    */
    private String bizFlowName;
    /**
     * 逻辑删除标识(0-否，1-是)
     */
    private Boolean delFlag;
}