package cn.jt.smile.service;

import cn.jt.smile.bean.params.role.AddAppRoleParams;
import cn.jt.smile.bean.params.role.CheckRoleNameParams;
import cn.jt.smile.bean.params.role.RoleQueryParams;
import cn.jt.smile.bean.params.role.UpdateAppRoleParams;
import cn.jt.smile.bean.po.ApproveRole;
import cn.jt.smile.bean.vo.ApproveRoleVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author smile
 */
public interface ApproveRoleService extends IService<ApproveRole> {

    /**
     * 新增审批用户组
     *
     * @param params 参数对象
     * @return 新增结果
     */
    boolean insert(AddAppRoleParams params);

    /**
     * 校验角色名称是否重复
     *
     * @param params 校验参数
     * @return 校验结果
     */
    boolean checkRoleName(CheckRoleNameParams params);

    /**
     * 修改
     *
     * @param params 修改参数对象
     * @return 修改结果
     */
    boolean updateRole(UpdateAppRoleParams params);

    /**
     * 业务数据ID逻辑删除
     *
     * @param id 业务数据ID
     * @return 删除结果
     */
    boolean deleteById(String id);

    /**
     * 根据业务ID查询详情
     *
     * @param id 业务数据ID
     * @return 查询结果对象
     */
    ApproveRoleVO getById(String id);

    /**
     * 分页查询
     *
     * @param page   分页参数
     * @param params 分页查询参数
     * @return 查询结果
     */
    IPage<ApproveRoleVO> getPage(Page<ApproveRole> page, RoleQueryParams params);

    /**
     * 根据审批角色ID移除流程名
     *
     * @param roleIds   审批角色ID列表
     * @param flowName  流程名称
     * @param tenantId  租户ID
     * @param productId 产品ID
     * @return 移除结果
     */
    @SuppressWarnings("UnusedReturnValue")
    boolean removeFlowName(List<String> roleIds, String flowName, Long tenantId, String productId);

    /**
     * 根据审批角色ID插入流程名
     *
     * @param roleIds   审批角色ID列表
     * @param flowName  流程名称
     * @param tenantId  租户ID
     * @param productId 产品ID
     * @return 插入结果
     */
    @SuppressWarnings("UnusedReturnValue")
    boolean insertFlowName(List<String> roleIds, String flowName, Long tenantId, String productId);

    /**
     * 列表查询
     *
     * @param params 查询参数
     * @return 查询结果
     */
    List<ApproveRoleVO> getList(RoleQueryParams params);

    /**
     * 检查用户是否存在流程配置
     * @param userId 用户ID
     * @return true-存在流程配置, false-不存在流程配置
     */
    boolean checkUserInFlow(String userId);
}
