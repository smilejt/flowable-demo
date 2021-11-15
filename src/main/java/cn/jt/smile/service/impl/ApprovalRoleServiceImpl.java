package cn.jt.smile.service.impl;

import cn.jt.smile.bean.params.role.*;
import cn.jt.smile.bean.po.ApproveRole;
import cn.jt.smile.bean.po.ApproveUser;
import cn.jt.smile.bean.vo.ApproveRoleVO;
import cn.jt.smile.bean.vo.ApproveUserVO;
import cn.jt.smile.mapper.ApproveRoleMapper;
import cn.jt.smile.service.ApproveRoleService;
import cn.jt.smile.service.ApproveUserService;
import cn.jt.smile.util.BeanCopy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author smile
 */
@Slf4j
@Service
public class ApprovalRoleServiceImpl extends ServiceImpl<ApproveRoleMapper, ApproveRole> implements ApproveRoleService {
    private static final String DEL_FLAG_KEY = "del_flag";
    private static final String PRODUCT_ID_KEY = "product_id";
    private static final String TENANT_ID_KEY = "tenant_id";
    private static final String ROLE_NAME_KEY = "role_name";
    private static final String APPROVE_NAME_KEY = "approve_name";
    private static final String ACCOUNT_NAME_KEY = "account_name";
    private static final String ROLE_ID_KEY = "role_id";
    private static final int NUMBER_ZERO = 0;

    @Resource
    private ApproveUserService appUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(AddAppRoleParams params) {
        log.info("[ApprovalUserServiceImpl].[add] ------> Add Role Start, params = {}", JSON.toJSONString(params));
        if (this.checkRoleName(BeanCopy.copy(params, CheckRoleNameParams.class))) {
            log.error("租户ID {} 产品ID {} 审批角色名称 {} 重复", params.getTenantId(), params.getProductId(), params.getRoleName());
            throw new RuntimeException("审批角色名称重复");
        }

        //审批角色对象
        ApproveRole role = BeanCopy.copy(params, ApproveRole.class);
        role.setDelFlag(false);
        //查询用户名
        queryUserInfo(params, role);

        int insertNum = baseMapper.insert(role);
        if (insertNum > 0 && !CollectionUtils.isEmpty(params.getUserList())) {
            addRoleUser(params, role);
        }
        log.info("[ApprovalUserServiceImpl].[add] ------> Add Role End, insertNum = {}, role = {}", insertNum, JSON.toJSONString(role));
        return insertNum > 0;
    }

    @Override
    public boolean checkRoleName(CheckRoleNameParams params) {
        log.info("[ApprovalUserServiceImpl].[checkRoleName] ------> Check Role Name By Tenant Id Start, params = {}", JSON.toJSONString(params));
        QueryWrapper<ApproveRole> qw = new QueryWrapper<>();
        qw.eq(DEL_FLAG_KEY, false);
        qw.eq(PRODUCT_ID_KEY, params.getProductId());
        qw.eq(TENANT_ID_KEY, params.getTenantId());
        qw.eq(ROLE_NAME_KEY, params.getRoleName());
        //有ID参数,视为修改,同ID可以重复(不修改角色名称的情况)
        if (!ObjectUtils.isEmpty(params.getId())) {
            qw.ne(ROLE_ID_KEY, params.getId());
        }
        List<ApproveRole> roles = baseMapper.selectList(qw);
        boolean result = !CollectionUtils.isEmpty(roles);
        //但凡查出来列表不为空,都是有重复
        log.info("[ApprovalUserServiceImpl].[checkRoleName] ------> Check Role Name By Tenant Id End, result = {}", result);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(UpdateAppRoleParams params) {
        log.info("[ApprovalRoleServiceImpl].[updateRole] ------> Update Approve Role Start, params = {}", JSON.toJSONString(params));
        if (this.checkRoleName(BeanCopy.copy(params, CheckRoleNameParams.class))) {
            log.error("租户ID {} 产品ID {} 审批角色名称 {} 重复", params.getTenantId(), params.getProductId(), params.getRoleName());
            throw new RuntimeException("审批角色名称重复");
        }
        ApproveRole role = BeanCopy.copy(params, ApproveRole.class);
        //查询用户名
        queryUserInfo(params, role);
        role.setRoleId(params.getId());
        QueryWrapper<ApproveRole> qw = new QueryWrapper<>();
        qw.eq(ROLE_ID_KEY, params.getId());
        qw.eq(DEL_FLAG_KEY, false);
        int result = baseMapper.update(role, qw);

        if (result > 0 && !CollectionUtils.isEmpty(params.getUserList())) {
            //前端传唯一ID
            List<ApproveUserVO> approveUserList = appUserService.getApproveUserListByRoleId(params.getId());
            Set<String> approveIds = new HashSet<>();
            if (!CollectionUtils.isEmpty(approveUserList)) {
                approveUserList.forEach(approve -> approveIds.add(approve.getBizId()));
            }
            params.getUserList().forEach(user -> {
                if (!ObjectUtils.isEmpty(user.getBizId())) {
                    approveIds.remove(user.getBizId());
                }
            });
            //删除走物理删除
            if (!CollectionUtils.isEmpty(approveIds)) {
                appUserService.removeByIds(approveIds);
            }
            //新增及更新
            addRoleUser(params, role);
        }
        log.info("[ApprovalRoleServiceImpl].[updateRole] ------> Update Approve Role Start, result = {}", result);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(String id) {
        log.info("[ApprovalRoleServiceImpl].[deleteById] ------> Delete Role By Role Id Start, id = {}", id);
        ApproveRole role = getApproveRole(id);
        // 流程配置校验
        if (role.getApproveNumber() > NUMBER_ZERO) {
            throw new RuntimeException("当前用户组存在流程配置,不可删除");
        }
        role.setDelFlag(true);
        //删除角色数据
        int result = baseMapper.update(role, new QueryWrapper<ApproveRole>().lambda().eq(ApproveRole::getRoleId, id));
        deleteRoleUserByRoleId(id);
        log.info("[ApprovalRoleServiceImpl].[deleteById] ------> Delete Role By Role Id End, result = {}", result);
        return result > 0;
    }

    @Override
    public ApproveRoleVO getById(String id) {
        log.info("[ApprovalRoleServiceImpl].[getById] ------> Get Role By Role Id Start, id = {}", id);
        ApproveRole role = getApproveRole(id);
        ApproveRoleVO vo = BeanCopy.copy(role, ApproveRoleVO.class);
        getUserInfo(id, vo);
        log.info("[ApprovalRoleServiceImpl].[getById] ------> Get Role By Role Id End, vo = {}", JSON.toJSONString(vo));
        return vo;
    }

    @Override
    public IPage<ApproveRoleVO> getPage(Page<ApproveRole> page, RoleQueryParams params) {
        log.info("[ApprovalRoleServiceImpl].[getPage] ------> Page Query Approve Role By Params Start, page = {} params = {}", JSON.toJSONString(page), JSON.toJSONString(params));
        QueryWrapper<ApproveRole> qw = getQueryWrapper(params);
        IPage<ApproveRole> iPage = baseMapper.selectPage(page, qw);
        IPage<ApproveRoleVO> resultPage = BeanCopy.copyPage(iPage, ApproveRoleVO.class);
        log.info("[ApprovalRoleServiceImpl].[getPage] ------> Page Query Approve Role By Params End, resultPage.getRecords.size = {}", CollectionUtils.isEmpty(resultPage.getRecords()) ? null : resultPage.getRecords().size());
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeFlowName(List<String> roleIds, String flowName, Long tenantId, String productId) {
        log.info("[ApprovalRoleServiceImpl].[removeFlowName] ------> Remove Flow Name By Role Ids Start, roleIds = {}, flowName = {}, tenantId = {}, productId = {}", JSON.toJSONString(roleIds), flowName, tenantId, productId);
        List<ApproveRole> approveRoles = getApproveRolesByParams(roleIds, tenantId, productId);
        if (!CollectionUtils.isEmpty(approveRoles)) {
            //移除当前传入的流程名称
            approveRoles.forEach(role -> {
                String approveName = role.getApproveName();
                if (!ObjectUtils.isEmpty(approveName)) {
                    List<String> approveNames = Lists.newArrayList(approveName.split(","));
                    approveNames.removeIf(name -> name.equals(flowName));
                    role.setApproveNumber(approveNames.size());
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < approveNames.size(); i++) {
                        stringBuilder.append(approveNames.get(i));
                        if (i < approveNames.size() - 1) {
                            stringBuilder.append(",");
                        }
                    }
                    role.setApproveName(stringBuilder.toString());
                }
                baseMapper.updateById(role);
            });
            log.info("[ApprovalRoleServiceImpl].[removeFlowName] ------> Remove Flow Name By Role Ids End, return = true");
            return true;
        }
        log.info("[ApprovalRoleServiceImpl].[removeFlowName] ------> Remove Flow Name By Role Ids End, return = false");
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertFlowName(List<String> roleIds, String flowName, Long tenantId, String productId) {
        log.info("[ApprovalRoleServiceImpl].[insertFlowName] ------> Insert Flow Name By Role Ids Start, roleIds = {}, flowName = {}, tenantId = {}, productId = {}", JSON.toJSONString(roleIds), flowName, tenantId, productId);
        List<ApproveRole> approveRoles = getApproveRolesByParams(roleIds, tenantId, productId);
        if (!CollectionUtils.isEmpty(approveRoles)) {
            //插入当前传入的流程名称
            approveRoles.forEach(role -> {
                String approveName = role.getApproveName();
                if (!ObjectUtils.isEmpty(approveName)) {
                    role.setApproveNumber(role.getApproveNumber() + 1);
                    role.setApproveName(String.format("%s,%s", approveName, flowName));
                } else {
                    role.setApproveName(flowName);
                    role.setApproveNumber(1);
                }
                baseMapper.updateById(role);
            });
            log.info("[ApprovalRoleServiceImpl].[insertFlowName] ------> Insert Flow Name By Role Ids End, return = true");
            return true;
        }
        log.info("[ApprovalRoleServiceImpl].[insertFlowName] ------> Insert Flow Name By Role Ids End, return = false");
        return false;
    }

    @Override
    public List<ApproveRoleVO> getList(RoleQueryParams params) {
        log.info("[ApprovalRoleServiceImpl].[getList] ------> List Query Approve Role By Params Start, params = {}", JSON.toJSONString(params));
        QueryWrapper<ApproveRole> qw = getQueryWrapper(params);
        List<ApproveRole> roles = baseMapper.selectList(qw);
        List<ApproveRoleVO> resultList = BeanCopy.copyBatch(roles, ApproveRoleVO.class);
        if (CollectionUtils.isEmpty(resultList)) {
            resultList = Lists.newArrayList();
        }
        //补充用户信息
        resultList.forEach(vo -> getUserInfo(vo.getRoleId(), vo));
        log.info("[ApprovalRoleServiceImpl].[getList] ------> List Query Approve Role By Params Start, resultList.size = {}", resultList.size());
        return resultList;
    }

    @Override
    public boolean checkUserInFlow(String userId) {
        log.info("[ApprovalRoleServiceImpl].[checkUserInFlow] ------> Check User In Flows Start, params = {}", userId);
        boolean result = false;
        List<String> roleIdByUser = appUserService.getRoleIdByUser(userId);
        if (!CollectionUtils.isEmpty(roleIdByUser)) {
            List<ApproveRole> approveRoles = baseMapper.selectList(new QueryWrapper<ApproveRole>().lambda()
                    .eq(ApproveRole::getDelFlag, false)
                    .ne(ApproveRole::getApproveNumber, 0)
                    .in(ApproveRole::getRoleId, roleIdByUser));
            if (!CollectionUtils.isEmpty(approveRoles)) {
                result = true;
            }
        }
        log.info("[ApprovalRoleServiceImpl].[checkUserInFlow] ------> Check User In Flows End, result = {}", result);
        return result;
    }

    /**
     * 审批角色查询用户相关信息
     *
     * @param id 审批角色ID
     * @param vo 审批角色对象
     */
    private void getUserInfo(String id, ApproveRoleVO vo) {
        //查询关联用户信息
        List<ApproveUserVO> approveUsers = appUserService.getApproveUserListByRoleId(id);
        vo.setUserList(Lists.newArrayList());
        vo.setApproveDepartmentMap(new HashMap<>(2));
        if (!CollectionUtils.isEmpty(approveUsers)) {
            approveUsers.forEach(approveUser -> {
                if (!CollectionUtils.isEmpty(approveUser.getApproveDepartmentList())) {
                    vo.getApproveDepartmentMap().put(approveUser.getUserId(), approveUser.getApproveDepartmentList());
                }
                //TODO 根据用户ID查询用户信息并返回给前端
            });
        }
    }

    /**
     * 查询用户信息
     *
     * @param params 新增/更新参数
     * @param role   新增/更新对象
     */
    private void queryUserInfo(AddAppRoleParams params, ApproveRole role) {
        if (CollectionUtils.isEmpty(params.getUserList())) {
            role.setApproveName("");
            role.setAccountNumber(0);
        } else {
            //TODO 查询用户信息
            role.setAccountName("根据业务写");
            role.setAccountNumber(params.getUserList().size());
        }
    }

    /**
     * 组织查询对象
     *
     * @param params 查询参数
     * @return 查询对象
     */
    private QueryWrapper<ApproveRole> getQueryWrapper(RoleQueryParams params) {
        QueryWrapper<ApproveRole> qw = new QueryWrapper<>();
        qw.eq(DEL_FLAG_KEY, false);
        if (!Objects.isNull(params)) {
            if (!ObjectUtils.isEmpty(params.getProductId())) {
                qw.eq(PRODUCT_ID_KEY, params.getProductId());
            }
            if (!Objects.isNull(params.getTenantId())) {
                qw.eq(TENANT_ID_KEY, params.getTenantId());
            }
            if (!ObjectUtils.isEmpty(params.getRoleName())) {
                qw.like(ROLE_NAME_KEY, params.getRoleName());
            }
            if (!ObjectUtils.isEmpty(params.getApproveName())) {
                qw.like(APPROVE_NAME_KEY, params.getApproveName());
            }
            if (!ObjectUtils.isEmpty(params.getAccountName())) {
                qw.like(ACCOUNT_NAME_KEY, params.getAccountName());
            }
            if (!CollectionUtils.isEmpty(params.getIds())) {
                qw.in(ROLE_ID_KEY, params.getIds());
            }
        }
        return qw;
    }

    /**
     * 修改审批角色关联流程名条件查询
     *
     * @param roleIds   审批角色ID列表
     * @param tenantId  租户ID
     * @param productId 产品ID
     * @return 查询结果集合
     */
    private List<ApproveRole> getApproveRolesByParams(List<String> roleIds, Long tenantId, String productId) {
        QueryWrapper<ApproveRole> qw = new QueryWrapper<>();
        qw.eq(PRODUCT_ID_KEY, productId);
        qw.eq(TENANT_ID_KEY, tenantId);
        qw.eq(DEL_FLAG_KEY, false);
        qw.in(ROLE_ID_KEY, roleIds);
        return baseMapper.selectList(qw);
    }

    /**
     * 新增审批用户关联信息
     *
     * @param params 请求参数对象
     * @param role   新增角色对象
     */
    private void addRoleUser(AddAppRoleParams params, ApproveRole role) {
        params.getUserList().forEach(user -> {
            ApproveUser appUser = BeanCopy.copy(user, ApproveUser.class);
            appUser.setRoleGroupId(role.getRoleId());
            if (!CollectionUtils.isEmpty(user.getApproveDepartmentList())) {
                appUser.setApproveDepartment(JSON.toJSONString(user.getApproveDepartmentList()));
            } else {
                appUser.setApproveDepartment(JSON.toJSONString(Lists.newArrayList()));
            }
            appUser.setDelFlag(false);
            appUserService.save(appUser);
        });
    }

    /**
     * 逻辑删除审批角色关联用户
     *
     * @param id 审批角色业务ID
     */
    private void deleteRoleUserByRoleId(String id) {
        //删除角色关联用户数据
        if (appUserService.deleteByRoleId(id)) {
            log.info("关联用户信息删除成功， roleId = {}", id);
        } else {
            log.info("关联用户信息删除失败， roleId = {}", id);
        }
    }

    /**
     * 根据用户组ID查询用户组是否存在
     *
     * @param id 用户组ID
     * @return 查询结果
     */
    private ApproveRole getApproveRole(String id) {
        ApproveRole role = baseMapper.selectOne(new QueryWrapper<ApproveRole>().lambda().eq(ApproveRole::getDelFlag, false).eq(ApproveRole::getRoleId, id));
        if (Objects.isNull(role)) {
            log.error("删除数据不存在, id = {}", id);
            throw new RuntimeException("删除数据不存在");
        }
        return role;
    }
}
