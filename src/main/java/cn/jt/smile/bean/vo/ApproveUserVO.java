package cn.jt.smile.bean.vo;

import cn.jt.smile.bean.params.role.ApproveDepartmentParams;
import cn.jt.smile.bean.po.ApproveUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import java.util.List;

/**
 * 审批角色返回对象
 *
 * @author smile
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApproveUserVO extends ApproveUser {
    private static final long serialVersionUID = 6076284451836932274L;
    /**
     * 审批部门列表
     */
    private List<@Valid ApproveDepartmentParams> approveDepartmentList;
}
