package cn.jt.smile.service.impl;

import cn.jt.smile.bean.params.role.RoleQueryParams;
import cn.jt.smile.bean.vo.ApproveRoleVO;
import cn.jt.smile.service.ApproveRoleService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author smile
 */
@SuppressWarnings("unused")
@Slf4j
@Service
public class FakeLdapService {

    @Resource
    private ApproveRoleService approveRoleService;

    public int cycleFrequency(String jsonString, Object departmentId) {
        log.info("[FakeLdapService].[cycleFrequency] ------> 获取节点多实例基数, jsonString = {}, departmentId = {}", jsonString, departmentId);
        //通过org获取数据权限对应的数量
        return getEnableUserList(Lists.newArrayList(jsonString.split(",")), departmentId.toString()).size();
    }

    public List<String> findAllSales(String jsonString, Object departmentId) {
        log.info("[FakeLdapService].[findAllSales] ------> 获取配置的节点审批用户组, jsonString = {}, departmentId = {}", jsonString, departmentId);
        //通过org获取用户列表
        return getEnableUserList(Lists.newArrayList(jsonString.split(",")), departmentId.toString());
    }

    /**
     * 根据用户组ID获取启用用户ID集合
     *
     * @param groupIds 审批用户组ID集合
     * @return 全部用户ID集合
     */
    public List<String> getEnableUserList(List<String> groupIds, String departmentId) {
        log.info("[FakeLdapService].[getEnableUserList] ------> get user list start, groupIds = {}, departmentId = {}", JSON.toJSONString(groupIds), departmentId);
        if (CollectionUtils.isEmpty(groupIds)) {
            return Lists.newArrayList();
        }
        RoleQueryParams params = new RoleQueryParams();
        params.setIds(groupIds);
        List<ApproveRoleVO> list = approveRoleService.getList(params);
        if (CollectionUtils.isEmpty(list)) {
            // 这儿必须要抛错出去，终止启动流程
            throw new RuntimeException("请检查用户组是否存在");
        }
        Set<String> userIds = new HashSet<>();
        for (ApproveRoleVO vo : list) {
            if (!CollectionUtils.isEmpty(vo.getUserList())) {
                vo.getUserList().forEach(user -> {
                    Set<String> groupIdList = new HashSet<>();
                    if (vo.getApproveDepartmentMap().containsKey(user.getUserId())) {
                        vo.getApproveDepartmentMap().get(user.getUserId()).forEach(dep -> groupIdList.add(dep.getDepartmentId()));
                    }
                    if (groupIdList.contains(departmentId)) {
                        userIds.add(user.getUserId());
                    }
                });
            }
        }

        if (CollectionUtils.isEmpty(userIds)) {
            throw new RuntimeException("无可用审批用户");
        }
        return Lists.newArrayList(userIds);
    }
}
