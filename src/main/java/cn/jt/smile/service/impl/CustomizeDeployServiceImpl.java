package cn.jt.smile.service.impl;

import cn.jt.smile.bean.params.flow.SaveDeployParams;
import cn.jt.smile.bean.po.CustomizeDeploy;
import cn.jt.smile.bean.vo.CustomizeDeployVO;
import cn.jt.smile.mapper.CustomizeDeployMapper;
import cn.jt.smile.service.CustomizeDeployService;
import cn.jt.smile.util.BeanCopy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author smile
 */
@Slf4j
@Service
public class CustomizeDeployServiceImpl extends ServiceImpl<CustomizeDeployMapper, CustomizeDeploy> implements CustomizeDeployService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRecord(SaveDeployParams params) {
        log.info("[CustomizeDeployServiceImpl].[saveRecord] ------> Save Deploy Record Start, params = {}", JSON.toJSONString(params));
        if (ObjectUtils.isEmpty(params.getBusinessCode())) {
            throw new RuntimeException("业务模块编码为空");
        }
        if (Objects.isNull(params.getTenantId())) {
            throw new RuntimeException("租户ID为空");
        }
        if (ObjectUtils.isEmpty(params.getProductId())) {
            throw new RuntimeException("产品ID为空");
        }
        //查询流程是否存在
        CustomizeDeploy deploy = baseMapper.selectOne(new QueryWrapper<CustomizeDeploy>().lambda()
                .eq(CustomizeDeploy::getDelFlag, false)
                .eq(CustomizeDeploy::getTenantId, params.getTenantId())
                .eq(CustomizeDeploy::getProductId, params.getProductId())
                .eq(CustomizeDeploy::getBusinessCode, params.getBusinessCode()));
        //无数据,新增
        if (Objects.isNull(deploy)) {
            deploy = new CustomizeDeploy();
            deploy.setTenantId(params.getTenantId());
            deploy.setProductId(params.getProductId());
            deploy.setBusinessCode(params.getBusinessCode());
            deploy.setDelFlag(false);
            deploy.setGmtCreate(new Date());
            deploy.setSaveVersion(0);
        }
        //写入流程保存信息
        deploy.setSaveModelId(params.getSaveModelId());
        deploy.setSaveNodeData(Objects.isNull(params.getO()) ? JSON.toJSONString(new HashMap<>(1)) : JSON.toJSONString(params.getO()));
        deploy.setDeployStatus(false);
        deploy.setSaveVersion(Objects.isNull(deploy.getSaveVersion()) ? 1 : deploy.getSaveVersion() + 1);
        deploy.setIsEnable(params.getIsEnable());
        boolean result = super.saveOrUpdate(deploy);
        log.info("[CustomizeDeployServiceImpl].[saveRecord] ------> Save Deploy Record End, result = {}", result);
        return result;
    }

    @Override
    public CustomizeDeployVO getById(String deployId) {
        log.info("[CustomizeDeployServiceImpl].[getById] ------> Query Deploy Flow Info Start, deployId = {}", deployId);
        CustomizeDeploy deploy = baseMapper.selectOne(new QueryWrapper<CustomizeDeploy>().lambda()
                .eq(CustomizeDeploy::getDelFlag, false)
                .eq(CustomizeDeploy::getDeployId, deployId));
        CustomizeDeployVO resultDeploy = BeanCopy.copy(deploy, CustomizeDeployVO.class);
        log.info("[CustomizeDeployServiceImpl].[getById] ------> Query Deploy Flow Info End, Result Deploy = {}", JSON.toJSONString(resultDeploy));
        return resultDeploy;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deployFlow(String deployId) {
        log.info("[CustomizeDeployServiceImpl].[deployFlow] ------> User Save Flow Process Start, deployId = {}", deployId);
        CustomizeDeploy deploy = baseMapper.selectOne(new QueryWrapper<CustomizeDeploy>().lambda()
                .eq(CustomizeDeploy::getDelFlag, false)
                .eq(CustomizeDeploy::getDeployId, deployId));
        if (Objects.isNull(deploy)) {
            throw new RuntimeException("未找到该流程");
        }
        if (deploy.getDeployStatus()) {
            throw new RuntimeException("该流程已部署");
        }
        if (ObjectUtils.isEmpty(deploy.getSaveModelId())) {
            throw new RuntimeException("该流程未定义");
        }

        deploy.setDeployStatus(true);
        deploy.setDeployVersion(Objects.isNull(deploy.getDeployVersion()) ? 1 : deploy.getDeployVersion() + 1);
        deploy.setDeployModelId(deploy.getSaveModelId());
        deploy.setDeployNodeData(deploy.getSaveNodeData());
        int updateNum = baseMapper.updateById(deploy);
        log.info("[CustomizeDeployServiceImpl].[deployFlow] ------> User Save Flow Process End, updateNum = {}", updateNum);
        return updateNum > 0;
    }
}
