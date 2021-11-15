package cn.jt.smile.service.impl;

import cn.jt.smile.bean.params.role.BizFlowRoleParams;
import cn.jt.smile.bean.po.BizFlowRole;
import cn.jt.smile.mapper.BizFlowRoleMapper;
import cn.jt.smile.service.ApproveRoleService;
import cn.jt.smile.service.BizFlowRoleService;
import cn.jt.smile.util.BeanCopy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author smile
 */
@Slf4j
@Service
public class BizFlowRoleServiceImpl extends ServiceImpl<BizFlowRoleMapper, BizFlowRole> implements BizFlowRoleService {
    private static final String TENANT_ID_KEY = "tenant_id";
    private static final String DEL_FLAG_KEY = "del_flag";
    private static final String PRODUCT_ID_KEY = "product_id";
    private static final String BIZ_FLOW_ID_KEY = "biz_flow_id";
    private static final int NUMBER_ZERO = 0;

    @Resource
    private ApproveRoleService roleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSave(List<BizFlowRoleParams> params) {
        log.info("[BizFlowRoleServiceImpl].[batchSave] ------> Batch Save Flow To Role Start, params.size = {}", CollectionUtils.isEmpty(params) ? null : params.size());
        if (!CollectionUtils.isEmpty(params)) {
            //查询原关系列表
            QueryWrapper<BizFlowRole> qw = new QueryWrapper<>();
            qw.eq(TENANT_ID_KEY, params.get(NUMBER_ZERO).getTenantId());
            qw.eq(PRODUCT_ID_KEY, params.get(NUMBER_ZERO).getProductId());
            qw.eq(BIZ_FLOW_ID_KEY, params.get(NUMBER_ZERO).getBizFlowId());
            qw.eq(DEL_FLAG_KEY, false);
            List<BizFlowRole> bizFlowRoles = baseMapper.selectList(qw);
            if (!CollectionUtils.isEmpty(bizFlowRoles)) {
                Set<String> removeIds = new HashSet<>();
                //逻辑删除原流程关系
                bizFlowRoles.forEach(biz -> {
                    removeIds.add(biz.getRoleId());
                    biz.setDelFlag(true);
                    baseMapper.updateById(biz);
                });
                //审批角色移除当前关联流程名称
                roleService.removeFlowName(Lists.newArrayList(removeIds), params.get(NUMBER_ZERO).getBizFlowName(), params.get(NUMBER_ZERO).getTenantId(), params.get(NUMBER_ZERO).getProductId());
            }
            AtomicInteger insertNumber = new AtomicInteger(0);
            //新增流程关系
            Set<String> roleIds = new HashSet<>();
            params.forEach(param -> {
                if (!roleIds.contains(param.getRoleId())) {
                    BizFlowRole bizFlowRole = BeanCopy.copy(param, BizFlowRole.class);
                    bizFlowRole.setDelFlag(false);
                    insertNumber.addAndGet(baseMapper.insert(bizFlowRole));
                    roleIds.add(param.getRoleId());
                }
            });
            //审批角色插入当前关联流程名称
            roleService.insertFlowName(Lists.newArrayList(roleIds), params.get(NUMBER_ZERO).getBizFlowName(), params.get(NUMBER_ZERO).getTenantId(), params.get(NUMBER_ZERO).getProductId());
            log.info("[BizFlowRoleServiceImpl].[batchSave] ------> Batch Save Flow To Role End, Insert Number = {}", insertNumber.get());
            return true;
        }
        return false;
    }
}
