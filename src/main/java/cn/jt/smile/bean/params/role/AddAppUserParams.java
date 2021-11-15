package cn.jt.smile.bean.params.role;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 新增审批角色中的用户列表
 *
 * @author smile
 */
@Data
public class AddAppUserParams {

    private String bizId;

    /**
     * 审批用户ID
     */
    @NotNull(message = "审批用户ID不能为空")
    private String userId;

    /**
     * 审批部门列表
     */
    private List<@Valid ApproveDepartmentParams> approveDepartmentList;
}
