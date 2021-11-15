package cn.jt.smile.bean.vo;

import cn.jt.smile.bean.params.role.ApproveDepartmentParams;
import cn.jt.smile.bean.po.ApproveRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * @author smile
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApproveRoleVO extends ApproveRole {

    private static final long serialVersionUID = 6731383016338698053L;
    /**
     * 审批用户列表
     */
    private List<UserInfoVO> userList;

    /**
     * 用户审批范围集合
     */
    private Map<String, List<ApproveDepartmentParams>> approveDepartmentMap;
}
