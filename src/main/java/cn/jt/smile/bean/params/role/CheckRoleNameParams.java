package cn.jt.smile.bean.params.role;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author smile
 */
@Data
public class CheckRoleNameParams {

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 数据ID
     */
    private String id;
    
    /**
     * 角色组名称
     */
    @NotBlank(message = "角色名不能为空")
    private String roleName;
}
