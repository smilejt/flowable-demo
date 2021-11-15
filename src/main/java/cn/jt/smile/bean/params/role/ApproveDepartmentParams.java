package cn.jt.smile.bean.params.role;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 审批部门
 *
 * @author smile
 */
@Data
public class ApproveDepartmentParams {

    /**
     * 审批部门ID
     */
    @NotNull(message = "审批部门ID不能为空")
    private String departmentId;

    /**
     * 审批部门名称
     */
    private String departmentName;
}
