package cn.jt.smile.bean.params.role;

import cn.jt.smile.bean.base.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author smile
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleQueryParams extends AbstractEntity {

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
     * 账号名称
     */
    private String accountName;

    /**
     * 应用流程名称
     */
    private String approveName;

    /**
     * ID列表
     */
    private List<String> ids;
}
