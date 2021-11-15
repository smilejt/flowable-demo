package cn.jt.smile.service.impl;

import cn.jt.smile.bean.params.component.ComponentQueryParams;
import cn.jt.smile.bean.po.CustomizeComponent;
import cn.jt.smile.bean.vo.CustomizeComponentVO;
import cn.jt.smile.constant.InitComponentEnum;
import cn.jt.smile.constant.WorkflowTypeEnum;
import cn.jt.smile.mapper.CustomizeComponentMapper;
import cn.jt.smile.service.CustomizeComponentService;
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

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author smile
 */
@Slf4j
@Service
public class CustomizeComponentServiceImpl extends ServiceImpl<CustomizeComponentMapper, CustomizeComponent> implements CustomizeComponentService {
    private static final String DEL_FLAG_KEY = "del_flag";
    private static final String PRODUCT_ID_KEY = "product_id";
    private static final String TENANT_ID_KEY = "tenant_id";
    private static final Integer DEFAULT_TENANT_ID_VALUE = -1;
    private static final String WORKFLOW_TYPE_KEY = "workflow_type";

    @Override
    public List<CustomizeComponentVO> getComponentByType(ComponentQueryParams params) {
        log.info("[CustomizeComponentServiceImpl].[getComponentByType] ------> Query Flow Component By Type Start, params = {}", JSON.toJSONString(params));
        if (Objects.isNull(params.getWorkflowType())) {
            log.error("流程类型不能为空, params = {}", JSON.toJSONString(params));
            throw new RuntimeException("流程类型不能为空");
        }
        if (!ObjectUtils.isEmpty(params.getProductId())) {
            log.error("产品ID不能为空, params = {}", JSON.toJSONString(params));
            throw new RuntimeException("产品ID不能为空");
        }
        QueryWrapper<CustomizeComponent> qw = new QueryWrapper<>();
        qw.eq(DEL_FLAG_KEY, false);
        qw.eq(WORKFLOW_TYPE_KEY, params.getWorkflowType());
        qw.eq(PRODUCT_ID_KEY, params.getProductId());
        //无产品ID时默认
        if (!Objects.isNull(params.getTenantId())) {
            qw.eq(TENANT_ID_KEY, params.getTenantId());
        } else {
            qw.eq(TENANT_ID_KEY, DEFAULT_TENANT_ID_VALUE);
        }
        List<CustomizeComponent> list = baseMapper.selectList(qw);
        List<CustomizeComponentVO> result;
        if (CollectionUtils.isEmpty(list)) {
            result = Lists.newArrayList();
        } else {
            result = BeanCopy.copyBatch(list, CustomizeComponentVO.class);
        }
        log.info("[CustomizeComponentServiceImpl].[getComponentByType] ------> Query Flow Component By Type End, result.size = {}", result.size());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean initComponent(String productId, Long tenantId, List<Integer> initFlows) {
        log.info("[CustomizeComponentServiceImpl].[initComponent] ------> Init Flow Component By Product And Tenant Id Start, productId = {}, tenantId = {}, initFlows = {}", productId, tenantId, JSON.toJSONString(initFlows));
        if (CollectionUtils.isEmpty(initFlows)) {
            initFlows = Lists.newArrayList();
        }
        boolean result = init(productId, tenantId, initFlows) > 0;
        log.info("[CustomizeComponentServiceImpl].[initComponent] ------> Init Flow Component By Product And Tenant Id End, result = {}", result);
        return result;
    }

    /**
     * 初始化流程组件
     *
     * @param productId 产品ID
     * @param tenantId  租户ID
     * @param initFlows 初始化流程列表
     * @return 初始化结果
     */
    private int init(String productId, Long tenantId, List<Integer> initFlows) {
        log.info("Init Safe Flow Component Start, productId = {}, tenantId = {}, initFlows = {}", productId, tenantId, JSON.toJSONString(initFlows));
        AtomicInteger insertNum = new AtomicInteger(0);
        initFlows.forEach(type -> WorkflowTypeEnum.get(type).getIds().forEach(code -> {
            CustomizeComponent insert = BeanCopy.copy(InitComponentEnum.get(code), CustomizeComponent.class);
            insert.setProductId(productId);
            insert.setTenantId(tenantId);
            insert.setDelFlag(false);
            insert.setWorkflowType(WorkflowTypeEnum.get(type).getCode());
            insert.setGmtCreate(new Date());
            insert.setGmtModified(new Date());
            insertNum.addAndGet(baseMapper.insert(insert));
        }));
        log.info("Init Safe Flow Component End, insertNum = {}", insertNum.get());
        return insertNum.get();
    }
}
