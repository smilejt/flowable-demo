package cn.jt.smile.service.impl;

import cn.jt.smile.bean.params.role.ApproveDepartmentParams;
import cn.jt.smile.bean.po.ApproveUser;
import cn.jt.smile.bean.vo.ApproveUserVO;
import cn.jt.smile.mapper.ApproveUserMapper;
import cn.jt.smile.service.ApproveUserService;
import cn.jt.smile.util.BeanCopy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author smile
 */
@Slf4j
@Service
public class ApproveUserServiceImpl extends ServiceImpl<ApproveUserMapper, ApproveUser> implements ApproveUserService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByRoleId(String roleId) {
        log.info("[ApproveUserServiceImpl].[deleteByRoleId] ------> Delete Role User By Role Id Start, roleId = {}", roleId);
        ApproveUser user = new ApproveUser();
        user.setDelFlag(true);
        boolean result = super.update(user, new QueryWrapper<ApproveUser>().lambda().eq(ApproveUser::getRoleGroupId, roleId).eq(ApproveUser::getDelFlag, false));
        log.info("[ApproveUserServiceImpl].[deleteByRoleId] ------> Delete Role User By Role Id End, result = {}", result);
        return result;
    }

    @Override
    public List<ApproveUserVO> getApproveUserListByRoleId(String roleId) {
        log.info("[ApproveUserServiceImpl].[getApproveUserListByRoleId] ------> Get Approve User By Role Id Start, roleId = {}", roleId);
        List<ApproveUser> approveUsers = baseMapper.selectList(new QueryWrapper<ApproveUser>().lambda().eq(ApproveUser::getDelFlag, false).eq(ApproveUser::getRoleGroupId, roleId));
        List<ApproveUserVO> voList = BeanCopy.copyBatch(approveUsers, ApproveUserVO.class);
        if (!CollectionUtils.isEmpty(voList)) {
            voList.forEach(vo -> {
                if (!ObjectUtils.isEmpty(vo.getApproveDepartment()) && !CollectionUtils.isEmpty(JSON.parseArray(vo.getApproveDepartment(), ApproveDepartmentParams.class))) {
                    vo.setApproveDepartmentList(JSON.parseArray(vo.getApproveDepartment(), ApproveDepartmentParams.class));
                }
            });
        }
        log.info("[ApproveUserServiceImpl].[getApproveUserListByRoleId] ------> Get Approve User By Role Id End, voList = {}", CollectionUtils.isEmpty(voList) ? null : voList.size());
        return voList;
    }

    @Override
    public List<String> getRoleIdByUser(String userId) {
        log.info("[ApproveUserServiceImpl].[getRoleIdByUser] ------> Get Role Id By User Id Start, userId = {}", userId);
        List<String> roleIds = Lists.newArrayList();
        List<ApproveUser> approveUsers = baseMapper.selectList(new QueryWrapper<ApproveUser>().lambda()
                .eq(ApproveUser::getUserId, userId)
                .eq(ApproveUser::getDelFlag, false));
        if (!CollectionUtils.isEmpty(approveUsers)){
            approveUsers.forEach(approveUser -> roleIds.add(approveUser.getRoleGroupId()));
        }
        log.info("[ApproveUserServiceImpl].[getRoleIdByUser] ------> Get Role Id By User Id End, roleIds = {}", JSON.toJSONString(roleIds));
        return roleIds;
    }
}
