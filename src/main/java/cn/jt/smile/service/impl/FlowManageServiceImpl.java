package cn.jt.smile.service.impl;

import cn.jt.smile.bean.params.approval.SaveFlowParams;
import cn.jt.smile.bean.params.flow.ApproveConfigParams;
import cn.jt.smile.bean.params.flow.SaveDeployParams;
import cn.jt.smile.bean.params.flow.ViewParams;
import cn.jt.smile.constant.ApproveTitleEnum;
import cn.jt.smile.service.*;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author smile
 */
@Slf4j
@Service
public class FlowManageServiceImpl implements FlowManageService {

    @Resource
    private FlowApiService apiService;
    @Resource
    private CustomizeDeployService deployService;
    @Resource
    private FlowBpmnService bpmnService;
    @Resource
    private FlowAndViewService viewService;
    @Resource
    private TaskUserService userService;
    @Resource
    private ApproveRoleService roleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveModel(Object o) {
        log.info("[FlowManageServiceImpl].[saveModel] ------> User Save Flow Model Start");

        //请求参数转为对象
        ApproveConfigParams configForm = JSON.parseObject(JSON.toJSONString(o), ApproveConfigParams.class);
        checkParams(configForm);
        List<ViewParams> views = Lists.newArrayList();
        //流程保存
        BpmnModel bpmnXml = this.getBpmnXml(configForm, views);
        SaveFlowParams flowParams = new SaveFlowParams();
        flowParams.setBpmnModel(bpmnXml);
        flowParams.setModelName(configForm.getFlowName());
        String modelId = apiService.saveFlow(flowParams);
        if (!CollectionUtils.isEmpty(views)) {
            views.forEach(view -> view.setDefId(modelId));
            boolean viewResult = viewService.batchSave(views);
            log.info("[FlowManageServiceImpl].[saveModel] ------> Batch Save View Relation End, result = {}", viewResult);
        }
        //保存流程部署信息
        SaveDeployParams deployParams = new SaveDeployParams();
        deployParams.setSaveModelId(modelId);
        deployParams.setO(o);
        //流程保存相关信息
        deployParams.setBusinessCode(configForm.getBusinessCode());
        deployParams.setTenantId(configForm.getTenantId());
        deployParams.setProductId(configForm.getProductId());
        deployParams.setIsEnable(configForm.getFlowStats());
        boolean result = deployService.saveRecord(deployParams);
        log.info("[FlowManageServiceImpl].[saveModel] ------> User Save Flow Model End, result = {}", result);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deployFlow(String deployId) {
        log.info("[FlowManageServiceImpl].[deployFlow] ------> User Save Flow Process Start, deployId = {}", deployId);
        boolean result = deployService.deployFlow(deployId);
        log.info("[FlowManageServiceImpl].[deployFlow] ------> User Save Flow Process End, result = {}", result);
        return result;
    }

    @Override
    public boolean checkUserInFlowOrTask(String userId) {
        log.info("[FlowManageServiceImpl].[checkUserInFlowOrTask] ------> Check User In Flow Or Task Start, userId = {}", userId);
        if (ObjectUtils.isEmpty(userId)) {
            throw new RuntimeException("用户ID不能为空");
        }
        boolean result = userService.checkUserExistTask(userId);
        if (result) {
            throw new RuntimeException("待删除用户存在未完成任务");
        }
        result = roleService.checkUserInFlow(userId);
        if (result) {
            throw new RuntimeException("待删除用户存在流程配置");
        }
        log.info("[FlowManageServiceImpl].[checkUserInFlowOrTask] ------> Check User In Flow Or Task End, userId = {}", true);
        return true;
    }

    /**
     * 解析前端参数并组装Bpmn20.xml字符串
     *
     * @return Bpmn20.xml字符串
     */
    private BpmnModel getBpmnXml(ApproveConfigParams configForm, List<ViewParams> views) {
        return bpmnService.createFlowBpmn(configForm.getFlowChart().getNodes(), configForm.getFlowChart().getEdges(),
                getDocumentation(configForm.getSucTitle(), configForm.getSucTitleCode(), configForm.getFaiTitle(), configForm.getFaiTitleCode()),
                configForm.getBusinessCode(), configForm.getFlowName(), configForm.getTenantId(), configForm.getProductId(), views);
    }

    /**
     * 获取描述信息
     *
     * @param sucTitle 审批通过标题
     * @param sucCode  通过代码
     * @param faiTitle 审批失败标题
     * @param faiCode  失败代码
     * @return 组装后的描述信息
     */
    private static String getDocumentation(String sucTitle, Integer sucCode, String faiTitle, Integer faiCode) {
        sucTitle = String.format("%s(%s)", sucTitle, ApproveTitleEnum.getDescByCode(sucCode));
        faiTitle = String.format("%s(%s)", faiTitle, ApproveTitleEnum.getDescByCode(faiCode));
        return sucTitle.concat(",").concat(faiTitle);
    }

    /**
     * 保存流程参数检查
     *
     * @param configForm 流程保存对象
     */
    private void checkParams(ApproveConfigParams configForm) {
        if (Objects.isNull(configForm)) {
            throw new RuntimeException("流程参数解析失败!");
        }
        if (ObjectUtils.isEmpty(configForm.getFlowName())) {
            throw new RuntimeException("业务流程名称为空!");
        }
        if (ObjectUtils.isEmpty(configForm.getBusinessCode())) {
            throw new RuntimeException("业务流程编码为空!");
        }
        if (Objects.isNull(configForm.getTenantId())) {
            throw new RuntimeException("租户ID为空!");
        }
        if (ObjectUtils.isEmpty(configForm.getProductId())) {
            throw new RuntimeException("产品ID为空!");
        }
        if (Objects.isNull(configForm.getFlowStats())) {
            throw new RuntimeException("业务流程状态不正确!");
        }
    }
}
