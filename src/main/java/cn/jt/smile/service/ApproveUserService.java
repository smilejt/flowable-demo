package cn.jt.smile.service;

import cn.jt.smile.bean.po.ApproveUser;
import cn.jt.smile.bean.vo.ApproveUserVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author smile
 */
public interface ApproveUserService extends IService<ApproveUser> {

    /**
     * 根据角色ID逻辑删除
     *
     * @param roleId 角色ID
     * @return 删除结果
     */
    boolean deleteByRoleId(String roleId);

    /**
     * 根据审批角色ID查询关联用户列表
     *
     * @param roleId 审批角色ID
     * @return 查询结果集合
     */
    List<ApproveUserVO> getApproveUserListByRoleId(String roleId);

    /**
     * 根据用户ID查询审批角色信息
     * @param userId 用户ID
     * @return 查询结果RoleId集合
     */
    List<String> getRoleIdByUser(String userId);
}
