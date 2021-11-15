package cn.jt.smile.bean.params.role;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 新增审批角色
 *
 * @author smile
 */
@Data
public class AddAppRoleParams {

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 角色名称(0-10个字符)
     */
    @NotBlank(message = "角色名称不能为空")
    @Length(max = 10, message = "角色名称最多10个字符")
    private String roleName;

    /**
     * 是否临时用户(true-是, false-否, 默认否)
     */
    private Boolean temporary = false;

    /**
     * 备注
     */
    private String remark;

    /**
     * 角色账号列表
     */
    @NotNull(message = "审批用户列表不能为空")
    @Size(min = 1,message = "审批用户列表不能为空")
    private List<@Valid AddAppUserParams> userList;
}
