package cn.jt.smile.service.impl;

import cn.jt.smile.bean.params.approval.CompleteTaskParams;
import cn.jt.smile.bean.params.approval.SaveFlowParams;
import cn.jt.smile.bean.params.approval.StartFlowParams;
import cn.jt.smile.bean.params.condition.ConditionQueryParams;
import cn.jt.smile.bean.params.instance.AddInstanceParams;
import cn.jt.smile.bean.params.instance.QueryTaskUserParams;
import cn.jt.smile.bean.params.instance.UpdateTaskUserParams;
import cn.jt.smile.bean.vo.CustomizeConditionVO;
import cn.jt.smile.bean.vo.CustomizeDeployVO;
import cn.jt.smile.bean.vo.RunFlowVO;
import cn.jt.smile.bean.vo.TaskUserVO;
import cn.jt.smile.constant.AuditStatusEnum;
import cn.jt.smile.constant.FlowTaskActiveEnum;
import cn.jt.smile.service.*;
import cn.jt.smile.util.BeanCopy;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.javax.el.ELException;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author smile
 */
@Slf4j
@Service
public class FlowApiServiceImpl implements FlowApiService {
    private static final String DEPARTMENT_ID_KEY = "departmentId";
    private static final String TASK_DEFINITION_KEY = "taskDefinitionKey";

    @Resource
    private RuntimeService runtimeService;
    @Resource
    private IdentityService identityService;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private CustomizeDeployService deployService;
    @Resource
    private TaskService taskService;
    @Resource
    private CustomizeInstanceService instanceService;
    @Resource
    private CustomizeConditionService conditionService;
    @Resource
    private TaskUserService taskUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveFlow(SaveFlowParams params) {
        log.info("[FlowApiServiceImpl].[saveFlow] ------> Save Flow Model Start, params = {}", JSON.toJSONString(params));
        try {
            //流程部署
            if (Objects.isNull(params.getBpmnModel())) {
                throw new RuntimeException("流程定义XML字符串为空");
            }
            if (ObjectUtils.isEmpty(params.getModelName())) {
                throw new RuntimeException("流程模版名称为空");
            }
            Deployment deploy = repositoryService.createDeployment().addBpmnModel(String.format("%s.bpmn20.xml", UUID.randomUUID()), params.getBpmnModel()).deploy();
            String deployId = deploy.getId();
            ProcessDefinition process = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
            log.info("[FlowApiServiceImpl].[saveFlow] ------> Save Flow Model End, deploy.getId() = {}", process.getId());
            return process.getId();
        } catch (Exception e) {
            log.error("[FlowApiServiceImpl].[saveFlow] ------> Save Flow Model Error, e:", e);
            throw new RuntimeException("流程部署失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RunFlowVO startFlow(StartFlowParams params) {
        log.info("[FlowApiServiceImpl].[startFlow] ------> Business System Startup Process, Start, params = {}", JSON.toJSONString(params));
        RunFlowVO vo = new RunFlowVO();
        CustomizeDeployVO deploy = deployService.getById(params.getDeployId());
        //存在流程才启动流程
        if (!Objects.isNull(deploy) && !ObjectUtils.isEmpty(deploy.getDeployModelId())) {
            // 设置发起人
            identityService.setAuthenticatedUserId(params.getUserId());
            //设置条件参数
            Map<String, Object> varMap = new HashMap<>(8);
            //写入数据权限部门
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(params.getO()));
            varMap.put(DEPARTMENT_ID_KEY, jsonObject.get(DEPARTMENT_ID_KEY));
            ConditionQueryParams conditionParams = new ConditionQueryParams();
            conditionParams.setBusinessCode(params.getBusinessCode());
            conditionParams.setProductId(params.getProductId());
            conditionParams.setTenantId(params.getTenantId());
            List<CustomizeConditionVO> list = conditionService.getList(conditionParams);
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(condition -> varMap.put(condition.getConditionKey(), jsonObject.get(condition.getConditionKey())));
            }
            ProcessInstance processInstance;
            try {
                processInstance = runtimeService.startProcessInstanceById(deploy.getDeployModelId(), varMap);
            } catch (Exception e) {
                //捕捉EL表达式中抛出的异常
                if (e.getCause() instanceof ELException && e.getCause().getCause() instanceof RuntimeException) {
                    throw new RuntimeException(e.getCause().getCause().getMessage());
                }
                log.info("[FlowApiServiceImpl].[startFlow] ------> Business System Startup Process, Start, params = {}", JSON.toJSONString(params));
                log.error("[FlowApiServiceImpl].[startFlow] ------> Business System Startup Process, Error, e:", e);
                throw new RuntimeException("启动流程异常:" + e.getMessage());
            }
            vo.setInstanceId(processInstance.getProcessInstanceId());
            if (this.checkFlowEnd(processInstance.getProcessInstanceId())) {
                vo.setState(AuditStatusEnum.PASS.getCode());
                vo.setStateDescribe(AuditStatusEnum.PASS.getDescribe());
            } else {
                vo.setState(AuditStatusEnum.AUDITING.getCode());
                vo.setStateDescribe(AuditStatusEnum.AUDITING.getDescribe());
            }
        } else {
            vo.setState(AuditStatusEnum.PASS.getCode());
            vo.setStateDescribe(AuditStatusEnum.PASS.getDescribe());
        }

        AddInstanceParams instance = BeanCopy.copy(params, AddInstanceParams.class);
        instance.setDefId(deploy.getDeployModelId());
        instance.setInstanceId(vo.getInstanceId());
        instance.setDataId(params.getDataKey());
        instance.setPutAuditsTime(new Date());
        //TODO 获取上下文中的用户
        instance.setPutAuditsUserid("");
        instance.setFlowStatus(vo.getState());
        boolean saveInstance = instanceService.save(instance);
        log.info("[FlowApiServiceImpl].[startFlow] ------> Save Flow Instance End, result = {}", saveInstance);
        log.info("[FlowApiServiceImpl].[startFlow] ------> Business System Startup Process, End, vo = {}", JSON.toJSONString(vo));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RunFlowVO runTask(CompleteTaskParams params) {
        log.info("[FlowApiServiceImpl].[runTask] ------> Business System Execution Tasks Start, params = {}", JSON.toJSONString(params));
        if (ObjectUtils.isEmpty(params.getTaskId())) {
            throw new RuntimeException("任务ID不能为空");
        }
        // 使用任务id,获取任务对象，获取流程实例id
        Task task = taskService.createTaskQuery().taskId(params.getTaskId()).singleResult();
        if (Objects.isNull(task)) {
            throw new RuntimeException("任务不存在或已执行");
        }
        //创建返回对象
        RunFlowVO vo = new RunFlowVO();
        //利用任务对象，获取流程实例id
        String instancesId = task.getProcessInstanceId();
        vo.setInstanceId(instancesId);
        //查询该流程节点下其他待执行任务的用户
        QueryTaskUserParams taskParams = new QueryTaskUserParams();
        taskParams.setTaskId(params.getTaskId());
        Map<String, TaskUserVO> taskMap = taskUserService.getMap(taskParams);

        //写审核操作意见 arc_hi_comment
        taskService.addComment(params.getTaskId(), instancesId, params.getComments());
        //任务执行参数对象
        Map<String, Object> variables = new HashMap<>(2);
        variables.put("approved", params.isApproved());
        variables.put("comments", params.getComments());

        try {
            taskService.complete(params.getTaskId(), variables);
            vo.setState(AuditStatusEnum.AUDITING.getCode());
            vo.setStateDescribe(AuditStatusEnum.AUDITING.getDescribe());
        } catch (Exception e) {
            //TODO 任务执行异常,挂起流程
            throw new RuntimeException("任务执行失败,请检查后重试");
        }
        Map<String, String> activeTask = getActiveTask(instancesId);
        //更新TaskUser表
        List<UpdateTaskUserParams> updateList = Lists.newArrayList();
        if (activeTask.isEmpty() || !task.getTaskDefinitionKey().equals(activeTask.get(TASK_DEFINITION_KEY))) {
            //没有下级节点或当前节点执行完毕
            for (String userId : taskMap.keySet()) {
                UpdateTaskUserParams updateParams = new UpdateTaskUserParams();
                updateList.add(updateParams);
                updateParams.setBizId(taskMap.get(userId).getBizId());
                //TODO 替换 2333 为上下文中的用户ID
                if ("2333".equals(userId)){
                    updateParams.setActive(FlowTaskActiveEnum.USER_EXECUTION.getCode());
                }else {
                    updateParams.setActive(FlowTaskActiveEnum.SYSTEM_EXECUTION.getCode());
                }
            }
            if (activeTask.isEmpty()){
                if (params.isApproved()){
                    vo.setState(AuditStatusEnum.PASS.getCode());
                    vo.setStateDescribe(AuditStatusEnum.PASS.getDescribe());
                }else {
                    vo.setState(AuditStatusEnum.FAIL.getCode());
                    vo.setStateDescribe(AuditStatusEnum.FAIL.getDescribe());
                }
            }
        }else {
            for (String userId : taskMap.keySet()) {
                //TODO 替换 2333 为上下文中的用户ID
                if ("2333".equals(userId)){
                    UpdateTaskUserParams updateParams = new UpdateTaskUserParams();
                    updateList.add(updateParams);
                    updateParams.setBizId(taskMap.get(userId).getBizId());
                    updateParams.setActive(FlowTaskActiveEnum.USER_EXECUTION.getCode());
                }
            }
        }
        boolean updateResult = taskUserService.batchUpdateActive(updateList);
        log.info("[FlowApiServiceImpl].[runTask] ------> Update Task User End, result = {}", updateResult);

        log.info("[FlowApiServiceImpl].[runTask] ------> Business System Execution Tasks End, vo = {}", JSON.toJSONString(vo));
        return vo;
    }

    @Override
    public boolean checkFlowEnd(String instanceId) {
        log.info("[FlowApiServiceImpl].[startFlow] ------> Check Whether The Process Is Completed Start, instanceId = {}", instanceId);
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        log.info("[FlowApiServiceImpl].[startFlow] ------> Check Whether The Process Is Completed End, result = {}", Objects.isNull(processInstance));
        return Objects.isNull(processInstance);
    }

    @Override
    public Map<String, String> getActiveTask(String instanceId) {
        log.info("[FlowApiServiceImpl].[getActiveTask] ------> Query Active Task Ids By Flow Instance Id Start, instanceId = {}", instanceId);
        Map<String, String> result = new ConcurrentHashMap<>(8);
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(instanceId).list();
        if (!CollectionUtils.isEmpty(taskList)) {
            result.put(TASK_DEFINITION_KEY, taskList.get(0).getTaskDefinitionKey());
            taskList.forEach(task -> result.put(task.getAssignee(), task.getId()));
        }
        log.info("[FlowApiServiceImpl].[getActiveTask] ------> Query Active Task Ids By Flow Instance Id End, result = {}", JSON.toJSONString(result));
        return result;
    }
}