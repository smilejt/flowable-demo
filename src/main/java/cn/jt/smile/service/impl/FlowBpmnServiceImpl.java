package cn.jt.smile.service.impl;

import cn.jt.smile.bean.params.condition.ConditionQueryParams;
import cn.jt.smile.bean.params.flow.*;
import cn.jt.smile.bean.params.role.BizFlowRoleParams;
import cn.jt.smile.bean.vo.CustomizeConditionVO;
import cn.jt.smile.constant.ConditionSymbolEnum;
import cn.jt.smile.constant.FlowAbleBpmnConstant;
import cn.jt.smile.service.BizFlowRoleService;
import cn.jt.smile.service.CustomizeConditionService;
import cn.jt.smile.service.FlowBpmnService;
import cn.jt.smile.util.BeanCopy;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.validation.ProcessValidator;
import org.flowable.validation.ProcessValidatorFactory;
import org.flowable.validation.ValidationError;
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
public class FlowBpmnServiceImpl implements FlowBpmnService {

    private static final int NUMBER_ZERO = 0;
    private static final int NUMBER_ONE = 1;
    private static final int NUMBER_TWO = 2;
    private static final int NUMBER_THREE = 3;
    private static String DOCUMENTATION;

    @Resource
    private CustomizeConditionService conditionService;
    @Resource
    private BizFlowRoleService bizFlowRoleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BpmnModel createFlowBpmn(List<ApproveNodeParams> nodes, List<ApproveEdgeParams> edges, String documentation, String businessCode, String businessName, Long tenantId, String productId, List<ViewParams> views) {
        log.info("[FlowBpmnServiceImpl].[createFlowBpmn] ------> Create Flow Bpmn20 XML Start, nodes.size = {}, edges.size = {}, documentation = {}, businessCode = {}, tenantId = {}, productId = {}",
                nodes.size(), edges.size(), documentation, businessCode, tenantId, productId);
        DOCUMENTATION = documentation;

        //定义开始、结束节点ID记录变量
        String startNodeId;
        String endNodeId;

        //获取全部用户及条件节点
        Map<String, ApproveNodeParams> userNodeMap = new HashMap<>(16);
        Map<String, ApproveNodeParams> conditionNodeMap = new HashMap<>(16);
        Map<String, List<ApproveEdgeParams>> edgeMap = new HashMap<>(16);

        //用于判断是否有孤立节点
        Set<String> nodeSet = new HashSet<>();
        Set<String> edgeSet = new HashSet<>();
        List<BizFlowRoleParams> forms = Lists.newArrayList();
        Map<String, String> idMap = this.getBasisDataMap(nodes, edges, userNodeMap, conditionNodeMap, edgeMap, forms, businessCode, businessName, nodeSet, edgeSet);
        //检查是否存在开始节点和结束节点
        if (!idMap.containsKey(FlowAbleBpmnConstant.MAP_START_NODE_KEY) || !idMap.containsKey(FlowAbleBpmnConstant.MAP_END_NODE_KEY)) {
            throw new RuntimeException("开始节点或结束节点不存在");
        } else {
            startNodeId = idMap.get(FlowAbleBpmnConstant.MAP_START_NODE_KEY);
            endNodeId = idMap.get(FlowAbleBpmnConstant.MAP_END_NODE_KEY);
        }

        //判断是否存在孤立节点
        edgeSet.forEach(nodeSet::remove);
        if (!CollectionUtils.isEmpty(nodeSet)) {
            throw new RuntimeException("不能存在孤立节点,请检查");
        }

        //检查条件节点参数
        checkParams(conditionNodeMap, edgeMap, businessCode, tenantId, productId);
        forms.forEach(form -> {
            form.setTenantId(tenantId);
            form.setProductId(productId);
        });
        //更新审批配置关系
        boolean saveResult = bizFlowRoleService.batchSave(forms);
        if (!saveResult) {
            throw new RuntimeException("保存流程与审批角色关系失败");
        }
        //开始组装bpmn规范xml字符串
        GetXmlStringParams params = new GetXmlStringParams();
        params.setBusinessCode(businessCode);
        params.setBusinessName(businessName);
        params.setConditionNodeMap(conditionNodeMap);
        params.setUserNodeMap(userNodeMap);
        params.setEdgeMap(edgeMap);
        params.setStartNodeId(startNodeId);
        params.setEndNodeId(endNodeId);
        params.setTenantId(tenantId);
        params.setProductId(productId);
        params.setViews(views);
        BpmnModel bpmnModel = getXmlString(params);
        log.info("[FlowBpmnServiceImpl].[createFlowBpmn] ------> Create Flow Bpmn20 XML End");
        return bpmnModel;
    }

    /**
     * 获取基础数据集
     *
     * @param nodes            前端传入用户、条件节点列表
     * @param edges            前端传入连接节点列表
     * @param userNodeMap      组装后的基础用户数据
     * @param conditionNodeMap 组装后的基础条件数据
     * @param edgeMap          组装后的连接节点数据
     * @param businessCode     流程业务编码
     * @param businessName     流程业务名称
     * @param edgeSet          连接节点列表(用于孤立节点判断)
     * @param nodeSet          节点列表(用于孤立节点判断)
     * @return 开始、结束节点ID数据集
     */
    private Map<String, String> getBasisDataMap(List<ApproveNodeParams> nodes, List<ApproveEdgeParams> edges, Map<String, ApproveNodeParams> userNodeMap,
                                                Map<String, ApproveNodeParams> conditionNodeMap, Map<String, List<ApproveEdgeParams>> edgeMap, List<BizFlowRoleParams> forms,
                                                String businessCode, String businessName, Set<String> nodeSet, Set<String> edgeSet) {
        log.info("[FlowBpmnServiceImpl].[getBasisDataMap] ------> Get Basis Data Map Start");
        Map<String, String> resultMap = new HashMap<>(2);
        //处理用户、条件节点
        if (!CollectionUtils.isEmpty(nodes)) {
            for (ApproveNodeParams node : nodes) {
                nodeSet.add(node.getId());
                if (FlowAbleBpmnConstant.NORMAL_NODE_MARK.equals(node.getShape())) {
                    if (Objects.isNull(node.getNodeType()) || CollectionUtils.isEmpty(node.getResIds())) {
                        throw new RuntimeException("部分节点内有必填项未填, 请检查");
                    }

                    userNodeMap.put(node.getId(), node);
                    if (!CollectionUtils.isEmpty(node.getResIds())) {
                        node.getResIds().forEach(id -> {
                            BizFlowRoleParams params = new BizFlowRoleParams();
                            params.setBizFlowId(businessCode);
                            params.setBizFlowName(businessName);
                            params.setRoleId(id);
                            forms.add(params);
                        });
                    }
                } else if (FlowAbleBpmnConstant.TEST_NODE_MARK.equals(node.getShape())) {
                    if (Objects.isNull(node.getRuleName()) || Objects.isNull(node.getParamType()) || Objects.isNull(node.getCondition())) {
                        throw new RuntimeException("部分节点内有必填项未填, 请检查");
                    }
                    conditionNodeMap.put(node.getId(), node);
                }

                //判断并记录开始和结束节点ID
                if (FlowAbleBpmnConstant.START_NODE_KEY.equals(node.getShape())) {
                    resultMap.put(FlowAbleBpmnConstant.MAP_START_NODE_KEY, node.getId());
                } else if (FlowAbleBpmnConstant.END_NODE_KEY.equals(node.getShape())) {
                    resultMap.put(FlowAbleBpmnConstant.MAP_END_NODE_KEY, node.getId());
                }
            }
        }

        //获取全部连接节点
        if (!CollectionUtils.isEmpty(edges)) {
            edges.forEach(edge -> {
                edgeSet.add(edge.getSource());
                edgeSet.add(edge.getTarget());
                if (!edgeMap.containsKey(edge.getSource())) {
                    edgeMap.put(edge.getSource(), Lists.newArrayList());
                }
                edgeMap.get(edge.getSource()).add(edge);
            });
        }

        log.info("[FlowBpmnServiceImpl].[getBasisDataMap] ------> Get Basis Data Map End");
        return resultMap;
    }

    /**
     * 校验参数
     *
     * @param conditionNodeMap 条件节点参数(shape = testNode 为条件节点)
     * @param edgeMap          连接节点信息
     * @param businessCode     流程业务编码
     * @param tenantId         租户ID
     * @param productId        产品ID
     */
    private void checkParams(Map<String, ApproveNodeParams> conditionNodeMap, Map<String, List<ApproveEdgeParams>> edgeMap, String businessCode, Long tenantId, String productId) {
        log.info("[FlowBpmnServiceImpl].[checkParams] ------> Check Flow Node Params Start");
        ConditionQueryParams params = new ConditionQueryParams();
        params.setProductId(productId);
        params.setTenantId(tenantId);
        params.setBusinessCode(businessCode);
        List<CustomizeConditionVO> conditionList = conditionService.getList(params);
        Map<String, CustomizeConditionVO> conditionMap = new HashMap<>(16);
        if (!CollectionUtils.isEmpty(conditionList)) {
            conditionList.forEach(condition -> conditionMap.put(condition.getConditionKey(), condition));
        }

        //校验条件参数
        for (String key : edgeMap.keySet()) {
            //判断是条件节点才校验
            if (conditionNodeMap.containsKey(edgeMap.get(key).get(NUMBER_ZERO).getTarget())) {
                if (edgeMap.get(key).size() <= 1) {
                    throw new RuntimeException("同一级条件不能只有一个条件节点");
                } else {
                    //条件判断Key
                    String conditionKey = null;
                    //下拉条件列表
                    List<ApproveNodeParams> selectList = Lists.newArrayList();
                    for (ApproveEdgeParams form : edgeMap.get(key)) {
                        //同一条件参数校验
                        if (ObjectUtils.isEmpty(conditionKey)) {
                            conditionKey = conditionNodeMap.get(form.getTarget()).getConditionKey();
                        } else {
                            if (!conditionKey.equals(conditionNodeMap.get(form.getTarget()).getConditionKey())) {
                                throw new RuntimeException("同一级条件节点只能选择同一条件参数");
                            }
                        }
                        if (NUMBER_ONE == conditionNodeMap.get(form.getTarget()).getParamType()) {
                            //下拉选择框
                            selectList.add(conditionNodeMap.get(form.getTarget()));
                        }
                    }
                    //调用方法选择校验下拉完整性
                    if (!CollectionUtils.isEmpty(selectList)) {
                        checkSelect(selectList, conditionMap.getOrDefault(conditionKey, null));
                    }
                }
            }
        }
    }

    /**
     * 下拉选择条件枚举完整性校验
     *
     * @param selectList 用户配置的选项列表
     * @param vo         该条件配置项的全部选值
     */
    @SuppressWarnings("unused")
    private void checkSelect(List<ApproveNodeParams> selectList, CustomizeConditionVO vo) {
        //TODO 下拉选项完整性校验
    }

    /**
     * 获取bpmn规范的xml字符串
     *
     * @param params 请求参数对象
     * @return xml字符串
     */
    private BpmnModel getXmlString(GetXmlStringParams params) {
        log.info("[FlowBpmnServiceImpl].[getXmlString] ------> Assemble BPMN Specification XML String Start");
        Map<String, ApproveNodeParams> userNodeMap = params.getUserNodeMap();
        Map<String, ApproveNodeParams> conditionNodeMap = params.getConditionNodeMap();
        Map<String, List<ApproveEdgeParams>> edgeMap = params.getEdgeMap();
        String startNodeId = params.getStartNodeId();
        String endNodeId = params.getEndNodeId();
        /*
         * 整合节点和连线成为一个 process
         */
        Process process = new Process();
        // 流程标识ID
        process.setId(FlowAbleBpmnConstant.getUid());
        //流程名称
        process.setName(params.getBusinessName());
        //流程开始节点
        StartEvent startNode = createStartNode();
        EndEvent endNode = createEndNode();
        EndEvent completeNode = createCompleteNode();
        process.addFlowElement(startNode);
        process.addFlowElement(endNode);
        process.addFlowElement(completeNode);
        //循环组装节点对象
        if (edgeMap.containsKey(startNodeId) && !CollectionUtils.isEmpty(edgeMap.get(startNodeId))) {
            //封装递归参数对象
            AssemblyProcessParams processParams = BeanCopy.copy(params, AssemblyProcessParams.class);
            processParams.setLastId(startNode.getId());
            processParams.setUserNodeMap(userNodeMap);
            processParams.setConditionNodeMap(conditionNodeMap);
            processParams.setEdgeMap(edgeMap);
            processParams.setEndNodeId(endNodeId);
            processParams.setBpmnEndNodeId(endNode.getId());
            processParams.setWaitNode(startNode);
            processParams.setCompleteNodeId(completeNode.getId());
            //判断开始节点之后是否是条件节点
            if (conditionNodeMap.containsKey(edgeMap.get(startNodeId).get(0).getTarget())) {
                for (ApproveEdgeParams edge : edgeMap.get(startNodeId)) {
                    processParams.setNextNodeId(edge.getTarget());
                    processParams.setIsStart(NUMBER_ZERO);
                    assemblyProcess(process, processParams);
                }
            } else {
                for (ApproveEdgeParams edge : edgeMap.get(startNodeId)) {
                    processParams.setNextNodeId(edge.getTarget());
                    processParams.setSequenceName(FlowAbleBpmnConstant.SEQUENCE_NAME_STRING);
                    processParams.setIsStart(NUMBER_ONE);
                    assemblyProcess(process, processParams);
                }
            }
        }
        log.info("[FlowBpmnServiceImpl].[getXmlString] ------> Assemble BPMN Specification XML String End");
        return getModelString(process);
    }


    /**
     * 组装Process对象
     *
     * @param process       Process对象
     * @param processParams 参数集合对象
     */
    private void assemblyProcess(Process process, AssemblyProcessParams processParams) {
        //如过下一个节点是结束节点
        if (processParams.getEndNodeId().equals(processParams.getNextNodeId())) {
            if (NUMBER_ZERO == processParams.getIsStart()) {
                SequenceFlow sucSequenceFlow = createSucSequenceFlow(processParams.getLastId(), processParams.getCompleteNodeId(), null);
                process.addFlowElement(sucSequenceFlow);
                ((ExclusiveGateway) processParams.getWaitNode()).setOutgoingFlows(Lists.newArrayList(sucSequenceFlow));
            } else {
                SequenceFlow sequenceFlow = createSequenceFlow(processParams.getLastId(), processParams.getCompleteNodeId());
                process.addFlowElement(sequenceFlow);
                ((StartEvent) processParams.getWaitNode()).setOutgoingFlows(Lists.newArrayList(sequenceFlow));
            }
        } else {
            if (processParams.getUserNodeMap().containsKey(processParams.getNextNodeId())) {
                //创建用户节点
                UserTask userTaskNode = createUserTaskNode(processParams.getUserNodeMap().get(processParams.getNextNodeId()));
                processParams.getViews().add(new ViewParams(processParams.getTenantId(), processParams.getProductId(), processParams.getNextNodeId(), userTaskNode.getId()));
                process.addFlowElement(userTaskNode);
                String lastNodeId = userTaskNode.getId();
                //是否有抄送
                if (!CollectionUtils.isEmpty(processParams.getUserNodeMap().get(processParams.getNextNodeId()).getUserIds())) {
                    ServiceTask serviceTask = createServiceTask(processParams.getUserNodeMap().get(processParams.getNextNodeId()).getUserIds(), processParams.getBusinessCode());
                    process.addFlowElement(serviceTask);
                    SequenceFlow sequenceFlow = createSequenceFlow(serviceTask.getId(), userTaskNode.getId());
                    process.addFlowElement(sequenceFlow);
                    lastNodeId = serviceTask.getId();
                }
                //创建网关节点
                ExclusiveGateway gateway = createGateway();
                process.addFlowElement(gateway);
                SequenceFlow gatewaySequence = createSequenceFlow(userTaskNode.getId(), gateway.getId());
                process.addFlowElement(gatewaySequence);
                //连接当前用户节点和上一级节点
                if (NUMBER_ZERO == processParams.getIsStart()) {
                    SequenceFlow sucSequenceFlow = createSucSequenceFlow(processParams.getLastId(), lastNodeId, null);
                    process.addFlowElement(sucSequenceFlow);
                } else {
                    SequenceFlow sequenceFlow = createSequenceFlow(processParams.getWaitNode().getId(), lastNodeId);
                    process.addFlowElement(sequenceFlow);
                }
                //拒绝节点
                SequenceFlow failSequenceFlow = createFailSequenceFlow(gateway.getId(), processParams.getBpmnEndNodeId());
                process.addFlowElement(failSequenceFlow);
                //获取下一集连接信息
                if (processParams.getEdgeMap().containsKey(processParams.getNextNodeId()) && !CollectionUtils.isEmpty(processParams.getEdgeMap().get(processParams.getNextNodeId()))) {
                    if (processParams.getConditionNodeMap().containsKey(processParams.getEdgeMap().get(processParams.getNextNodeId()).get(0).getTarget())) {
                        ExclusiveGateway sequenceGateway = getExclusiveGateway(process, gateway);
                        recursiveCall(process, processParams, sequenceGateway, processParams.getNextNodeId());
                    } else {
                        recursiveCall(process, processParams, gateway, processParams.getNextNodeId());
                    }
                }
            } else if (processParams.getConditionNodeMap().containsKey(processParams.getNextNodeId())) {
                //创建条件节点
                ApproveNodeParams approveNodeParams = processParams.getConditionNodeMap().get(processParams.getNextNodeId());
                RuleNodeParams ruleNode = BeanCopy.copy(approveNodeParams, RuleNodeParams.class);
                ruleNode.setKey(approveNodeParams.getConditionKey());
                ruleNode.setRuleName(approveNodeParams.getLabel());
                ruleNode.setRuleType(approveNodeParams.getParamType());
                //获取条件节点之后的用户节点连接信息
                ApproveEdgeParams approveEdgeForm = processParams.getEdgeMap().get(processParams.getNextNodeId()).get(0);
                if ((processParams.getEndNodeId().equals(approveEdgeForm.getTarget()))) {
                    //连接当前节点和上一级节点
                    SequenceFlow sucSequenceFlow = createSucSequenceFlow(processParams.getLastId(), processParams.getCompleteNodeId(), ruleNode);
                    processParams.getViews().add(new ViewParams(processParams.getTenantId(), processParams.getProductId(), processParams.getNextNodeId(), sucSequenceFlow.getId()));
                    process.addFlowElement(sucSequenceFlow);
                } else {
                    //创建用户节点信息
                    UserTask userTaskNode = createUserTaskNode(processParams.getUserNodeMap().get(approveEdgeForm.getTarget()));
                    processParams.getViews().add(new ViewParams(processParams.getTenantId(), processParams.getProductId(), approveEdgeForm.getTarget(), userTaskNode.getId()));
                    process.addFlowElement(userTaskNode);
                    String lastNodeId = userTaskNode.getId();
                    //是否有抄送
                    if (!CollectionUtils.isEmpty(processParams.getUserNodeMap().get(approveEdgeForm.getTarget()).getUserIds())) {
                        ServiceTask serviceTask = createServiceTask(processParams.getUserNodeMap().get(approveEdgeForm.getTarget()).getUserIds(), processParams.getBusinessCode());
                        process.addFlowElement(serviceTask);
                        SequenceFlow sequenceFlow = createSequenceFlow(serviceTask.getId(), userTaskNode.getId());
                        process.addFlowElement(sequenceFlow);
                        lastNodeId = serviceTask.getId();
                    }
                    //创建用户节点之后的网关节点
                    ExclusiveGateway gateway = createGateway();
                    process.addFlowElement(gateway);
                    SequenceFlow sequenceFlow = createSequenceFlow(userTaskNode.getId(), gateway.getId());
                    process.addFlowElement(sequenceFlow);
                    //连接当前节点和上一级节点
                    SequenceFlow sucSequenceFlow = createSucSequenceFlow(processParams.getLastId(), lastNodeId, ruleNode);
                    processParams.getViews().add(new ViewParams(processParams.getTenantId(), processParams.getProductId(), processParams.getNextNodeId(), sucSequenceFlow.getId()));
                    process.addFlowElement(sucSequenceFlow);
                    //拒绝节点
                    SequenceFlow failSequenceFlow = createFailSequenceFlow(gateway.getId(), processParams.getBpmnEndNodeId());
                    process.addFlowElement(failSequenceFlow);
                    //获取下一集连接信息
                    if (processParams.getEdgeMap().containsKey(approveEdgeForm.getTarget()) && !CollectionUtils.isEmpty(processParams.getEdgeMap().get(approveEdgeForm.getTarget()))) {
                        if (processParams.getConditionNodeMap().containsKey(processParams.getEdgeMap().get(approveEdgeForm.getTarget()).get(0).getTarget())) {
                            ExclusiveGateway sequenceGateway = getExclusiveGateway(process, gateway);
                            recursiveCall(process, processParams, sequenceGateway, approveEdgeForm.getTarget());
                        } else {
                            recursiveCall(process, processParams, gateway, approveEdgeForm.getTarget());
                        }
                    }
                }
            }
        }
    }

    /**
     * 组装递归调用参数
     *
     * @param process         bpmn对象
     * @param assemblyForm    参数对象
     * @param sequenceGateway 上级对象
     * @param target          下一节点ID
     */
    private void recursiveCall(Process process, AssemblyProcessParams assemblyForm, ExclusiveGateway sequenceGateway, String target) {
        for (ApproveEdgeParams edge : assemblyForm.getEdgeMap().get(target)) {
            AssemblyProcessParams copyForm = BeanCopy.copy(assemblyForm, AssemblyProcessParams.class);
            copyForm.setLastId(sequenceGateway.getId());
            copyForm.setNextNodeId(edge.getTarget());
            copyForm.setIsStart(NUMBER_ZERO);
            copyForm.setWaitNode(sequenceGateway);
            assemblyProcess(process, copyForm);
        }
    }

    /**
     * 获取网关节点之后的连线
     *
     * @param process bpmn对象
     * @param gateway 网关节点
     * @return 节点连线
     */
    private ExclusiveGateway getExclusiveGateway(Process process, ExclusiveGateway gateway) {
        ExclusiveGateway sequenceGateway = createGateway();
        process.addFlowElement(sequenceGateway);
        //同意
        SequenceFlow sequenceFlow = createSucSequenceFlow(gateway.getId(), sequenceGateway.getId(), null);
        process.addFlowElement(sequenceFlow);
        return sequenceGateway;
    }

    /**
     * 创建网关后的条件分支节点-审批失败
     *
     * @param sourceRef 上一级ID
     * @param targetRef 下一级ID
     * @return 失败条件
     */
    private SequenceFlow createFailSequenceFlow(String sourceRef, String targetRef) {
        SequenceFlow flow = new SequenceFlow();
        //上一级节点ID
        flow.setSourceRef(sourceRef);
        //下一级节点ID
        flow.setTargetRef(targetRef);
        //节点名称
        flow.setName("拒绝");
        //网关节点ID
        flow.setId(FlowAbleBpmnConstant.getUid());
        //没有条件分支,默认网关
        flow.setConditionExpression(FlowAbleBpmnConstant.FALSE_APPROVED_STRING);
        return flow;
    }

    /**
     * 创建网关节点
     *
     * @return 网关节点
     */
    private ExclusiveGateway createGateway() {
        ExclusiveGateway gateway = new ExclusiveGateway();
        gateway.setId(FlowAbleBpmnConstant.getUid());
        return gateway;
    }

    /**
     * 创建自动执行任务节点(目前用于抄送)
     *
     * @param users 抄送的用户ID
     * @return 创建结果
     */
    @SuppressWarnings("unused")
    private ServiceTask createServiceTask(List<String> users, String businessCode) {
        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(FlowAbleBpmnConstant.getUid());
        serviceTask.setName(FlowAbleBpmnConstant.COPY_NAME_STRING);

        serviceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        serviceTask.setImplementation(String.format("%s%s\", params, dataId%s", FlowAbleBpmnConstant.COPY_EXPRESSION_NAME_STRING, businessCode, FlowAbleBpmnConstant.CONDITION_END_STRING));
        return serviceTask;
    }

    /**
     * 创建用户节点
     *
     * @return 用户节点对象
     */
    private UserTask createUserTaskNode(ApproveNodeParams userNode) {
        ApproveNode approveNode = BeanCopy.copy(userNode, ApproveNode.class);
        approveNode.setNodeName(userNode.getLabel());
        UserTask userTask = new UserTask();
        userTask.setId(FlowAbleBpmnConstant.getUid());
        //写入节点名称
        userTask.setName(approveNode.getNodeName());
        //写入描述信息
        userTask.setDocumentation(DOCUMENTATION);
        //写入节点监听器
        userTask.setTaskListeners(getTaskListeners());
        userTask.setExecutionListeners(getExecutionListeners());
        //写入审批人EL表达式
        userTask.setAssignee(FlowAbleBpmnConstant.EL_ASSIGNEE_STRING);
        //设置多实例属性
        userTask.setLoopCharacteristics(getCountersign(approveNode.getResIds(), approveNode.getNodeType()));

        return userTask;
    }

    /**
     * 获取会签信息
     *
     * @param resIds 审批用户组
     * @param type   会签类型(1-或签[一票通过], 2-会签[全票通过])
     * @return 会签节点信息
     */
    private MultiInstanceLoopCharacteristics getCountersign(List<String> resIds, Integer type) {
        MultiInstanceLoopCharacteristics characteristics = new MultiInstanceLoopCharacteristics();
        // 设置并行执行(每个审批人可以同时执行）
        characteristics.setSequential(false);
        // 设置完成条件 提交会签类型，由FLowAble自定义方法处理结果
        characteristics.setCompletionCondition(
                String.format("%s%s%s", FlowAbleBpmnConstant.CONDITION_START_STRING, type, FlowAbleBpmnConstant.CONDITION_END_STRING));
        //审批人集合参数
        StringBuilder stringBuffer = new StringBuilder();
        for (String id : resIds) {
            stringBuffer.append(id).append(",");
        }

        //审批用户组
        characteristics.setInputDataItem(
                FlowAbleBpmnConstant.COLLECTION_START_STRING + stringBuffer.substring(0, stringBuffer.toString().length() - 1) + FlowAbleBpmnConstant.CARDINALITY_END_STRING);
        //循环次数获取方法
        characteristics.setLoopCardinality(
                FlowAbleBpmnConstant.CARDINALITY_START_STRING + stringBuffer.substring(0, stringBuffer.toString().length() - 1) + FlowAbleBpmnConstant.CARDINALITY_END_STRING);

        //迭代集合
        characteristics.setElementVariable(FlowAbleBpmnConstant.ASSIGNEE_STRING);

        return characteristics;
    }

    /**
     * 获取用户节点任务开始和结束监听器
     *
     * @return 监听器列表
     */
    private List<FlowableListener> getExecutionListeners() {
        List<FlowableListener> executionListeners = Lists.newArrayList();
        //监听器开始class
        FlowableListener listener = new FlowableListener();
        listener.setEvent(FlowAbleBpmnConstant.START_EVENT_ID);
        listener.setImplementationType(FlowAbleBpmnConstant.LISTENER_TYPE);
        listener.setImplementation(FlowAbleBpmnConstant.START_EXECUTION_CLASS_NAME_STRING);
        executionListeners.add(listener);

        //监听器结束class
        listener = new FlowableListener();
        listener.setEvent(FlowAbleBpmnConstant.END_EVENT_ID);
        listener.setImplementationType(FlowAbleBpmnConstant.LISTENER_TYPE);
        listener.setImplementation(FlowAbleBpmnConstant.END_EXECUTION_CLASS_NAME_STRING);
        executionListeners.add(listener);
        return executionListeners;
    }

    /**
     * 获取用户任务监听节点
     *
     * @return 监听节点列表
     */
    private List<FlowableListener> getTaskListeners() {
        List<FlowableListener> taskListeners = Lists.newArrayList();
        //监听器开始class
        FlowableListener listener = new FlowableListener();
        listener.setEvent(FlowAbleBpmnConstant.COMPLETE_EVENT_ID);
        listener.setImplementationType(FlowAbleBpmnConstant.LISTENER_TYPE);
        listener.setImplementation(FlowAbleBpmnConstant.TASK_CLASS_NAME_STRING);
        taskListeners.add(listener);
        return taskListeners;
    }

    /**
     * 单纯连接节点
     *
     * @param sourceRef 上一级ID
     * @param targetRef 下一级ID
     * @return 连接对象
     */
    private SequenceFlow createSequenceFlow(String sourceRef, String targetRef) {
        SequenceFlow flow = new SequenceFlow();
        //网关节点ID
        flow.setId(FlowAbleBpmnConstant.getUid());
        //上一级节点ID
        flow.setSourceRef(sourceRef);
        //下一级节点ID
        flow.setTargetRef(targetRef);
        return flow;
    }

    /**
     * 创建网关后的条件分支节点-审批通过
     *
     * @param sourceRef 上一级ID
     * @param targetRef 下一级ID
     * @param node      条件分支
     * @return 网关对象
     */
    private SequenceFlow createSucSequenceFlow(String sourceRef, String targetRef, RuleNodeParams node) {

        SequenceFlow flow = new SequenceFlow();
        //上一级节点ID
        flow.setSourceRef(sourceRef);
        //下一级节点ID
        flow.setTargetRef(targetRef);
        //条件分支节点ID
        flow.setId(FlowAbleBpmnConstant.getUid());

        if (Objects.isNull(node)) {
            //没有条件分支,默认条件分支
            flow.setConditionExpression(FlowAbleBpmnConstant.TRUE_APPROVED_STRING);
        } else {
            String condition;
            String nodeName = "";
            switch (ConditionSymbolEnum.get(node.getCondition())) {
                case NULL:
                    //为空
                    nodeName = String.format(ConditionSymbolEnum.NULL.getDescribe(), node.getKey());
                    condition = String.format(FlowAbleBpmnConstant.CONDITION_FORMAT_STRING_TWO, node.getKey(), ConditionSymbolEnum.NULL.getFlowSymbol());
                    break;
                case NOT_NULL:
                    //不为空
                    nodeName = String.format(ConditionSymbolEnum.NOT_NULL.getDescribe(), node.getKey());
                    condition = String.format(FlowAbleBpmnConstant.CONDITION_FORMAT_STRING_TWO, node.getKey(), ConditionSymbolEnum.NOT_NULL.getFlowSymbol());
                    break;
                case BELONG:
                    nodeName = String.format(ConditionSymbolEnum.BELONG.getDescribe(), node.getKey(), node.getValue());
                    List<Integer> integers = JSON.parseArray(node.getValue(), Integer.class);
                    StringBuilder temp = new StringBuilder();
                    if (!CollectionUtils.isEmpty(integers)) {
                        for (int index = 0; index < integers.size(); index++) {
                            if (0 != index) {
                                temp.append(" || ");
                            }
                            temp.append(String.format(ConditionSymbolEnum.BETWEEN.getFlowSymbol(), node.getKey(), integers.get(index)));
                        }
                    }
                    condition = String.format(FlowAbleBpmnConstant.CUSTOMIZE_CONDITION_FORMAT_STRING, temp);
                    break;
                case UN_BELONG:
                    nodeName = String.format(ConditionSymbolEnum.UN_BELONG.getDescribe(), node.getKey(), node.getValue());
                    List<Integer> integers2 = JSON.parseArray(node.getValue(), Integer.class);
                    StringBuilder temp2 = new StringBuilder();
                    if (!CollectionUtils.isEmpty(integers2)) {
                        for (int index = 0; index < integers2.size(); index++) {
                            if (0 != index) {
                                temp2.append(" && ");
                            }
                            temp2.append(String.format(ConditionSymbolEnum.UN_BELONG.getFlowSymbol(), node.getKey(), integers2.get(index)));
                        }
                    }
                    condition = String.format(FlowAbleBpmnConstant.CUSTOMIZE_CONDITION_FORMAT_STRING, temp2);
                    break;
                case UNEQUAL:
                    if (Objects.isNull(node.getRuleType()) || NUMBER_THREE != node.getRuleType()) {
                        nodeName = String.format(ConditionSymbolEnum.UNEQUAL.getDescribe(), node.getKey(), node.getValue());
                        condition = String.format(FlowAbleBpmnConstant.CONDITION_FORMAT_STRING, node.getKey(), ConditionSymbolEnum.UNEQUAL.getFlowSymbol(), node.getValue());
                    } else {
                        List<Integer> integers3 = JSON.parseArray(node.getValue(), Integer.class);
                        if (!CollectionUtils.isEmpty(integers3)) {
                            nodeName = String.format(ConditionSymbolEnum.UNEQUAL.getDescribe(), node.getKey(), node.getValue());
                            condition = String.format(FlowAbleBpmnConstant.CONDITION_FORMAT_STRING, node.getKey(), ConditionSymbolEnum.UNEQUAL.getFlowSymbol(), integers3.get(NUMBER_ZERO));
                        } else {
                            throw new RuntimeException("条件配置中存在‘不等于’条件参数错误");
                        }
                    }
                    break;
                case MORE_THAN:
                    nodeName = String.format(ConditionSymbolEnum.MORE_THAN.getDescribe(), node.getKey(), node.getValue());
                    condition = String.format(FlowAbleBpmnConstant.CONDITION_FORMAT_STRING, node.getKey(), ConditionSymbolEnum.MORE_THAN.getFlowSymbol(), node.getValue());
                    break;
                case LESS_THAN:
                    nodeName = String.format(ConditionSymbolEnum.LESS_THAN.getDescribe(), node.getKey(), node.getValue());
                    condition = String.format(FlowAbleBpmnConstant.CONDITION_FORMAT_STRING, node.getKey(), ConditionSymbolEnum.LESS_THAN.getFlowSymbol(), node.getValue());
                    break;
                case MORE_THAN_OR_EQUAL:
                    nodeName = String.format(ConditionSymbolEnum.MORE_THAN_OR_EQUAL.getDescribe(), node.getKey(), node.getValue());
                    condition = String.format(FlowAbleBpmnConstant.CONDITION_FORMAT_STRING, node.getKey(), ConditionSymbolEnum.MORE_THAN_OR_EQUAL.getFlowSymbol(), node.getValue());
                    break;
                case LESS_THAN_OR_EQUAL:
                    nodeName = String.format(ConditionSymbolEnum.LESS_THAN_OR_EQUAL.getDescribe(), node.getKey(), node.getValue());
                    condition = String.format(FlowAbleBpmnConstant.CONDITION_FORMAT_STRING, node.getKey(), ConditionSymbolEnum.LESS_THAN_OR_EQUAL.getFlowSymbol(), node.getValue());
                    break;
                case BETWEEN:
                    //介于x-y之间
                    nodeName = String.format(ConditionSymbolEnum.BETWEEN.getDescribe(), node.getKey(), node.getValue());
                    List<Integer> values = JSON.parseArray(node.getValue(), Integer.class);
                    if (values.size() < NUMBER_TWO) {
                        throw new RuntimeException("介于条件配置不正确");
                    }
                    condition = String.format("${%s %s %s && %s %s %s}", node.getKey(), ">", values.get(NUMBER_ZERO), node.getKey(), "<", values.get(NUMBER_ONE));
                    break;
                case EQUAL:
                    if (Objects.isNull(node.getRuleType()) || NUMBER_THREE != node.getRuleType()) {
                        nodeName = String.format(ConditionSymbolEnum.EQUAL.getDescribe(), node.getKey(), node.getValue());
                        condition = String.format(FlowAbleBpmnConstant.CONDITION_FORMAT_STRING, node.getKey(), ConditionSymbolEnum.EQUAL.getFlowSymbol(), node.getValue());
                    } else {
                        List<Integer> integers4 = JSON.parseArray(node.getValue(), Integer.class);
                        if (!CollectionUtils.isEmpty(integers4)) {
                            nodeName = String.format(ConditionSymbolEnum.EQUAL.getDescribe(), node.getKey(), node.getValue());
                            condition = String.format(FlowAbleBpmnConstant.CONDITION_FORMAT_STRING, node.getKey(), ConditionSymbolEnum.EQUAL.getFlowSymbol(), integers4.get(NUMBER_ZERO));
                        } else {
                            throw new RuntimeException("条件配置中存在‘等于’条件参数错误");
                        }
                    }
                    break;
                default:
                    condition = String.format(FlowAbleBpmnConstant.CONDITION_FORMAT_STRING, node.getKey(), node.getCondition(), node.getValue());
            }
            //配置条件分支网关(FlowAble排他网关)
            flow.setConditionExpression(condition);
            //条件分支名称
            flow.setName(nodeName);
        }
        return flow;
    }

    /**
     * 创建开始节点
     *
     * @return 开始节点对象
     */
    private StartEvent createStartNode() {
        //开始事件
        StartEvent startEvent = new StartEvent();
        startEvent.setId(FlowAbleBpmnConstant.START_EVENT_ID);
        startEvent.setName(FlowAbleBpmnConstant.START_EVENT_NAME);
        return startEvent;
    }

    /**
     * 创建拒绝结束节点
     *
     * @return 结束节点对象
     */
    private EndEvent createEndNode() {
        //结束事件--任务正常完成
        EndEvent completeEvent = new EndEvent();
        completeEvent.setId(FlowAbleBpmnConstant.END_EVENT_ID);
        completeEvent.setName(FlowAbleBpmnConstant.END_EVENT_NAME);
        return completeEvent;
    }

    /**
     * 创建拒绝结束节点
     *
     * @return 结束节点对象
     */
    private EndEvent createCompleteNode() {
        //结束事件--任务正常完成
        EndEvent completeEvent = new EndEvent();
        completeEvent.setId(FlowAbleBpmnConstant.COMPLETE_EVENT_ID);
        completeEvent.setName(FlowAbleBpmnConstant.COMPLETE_EVENT_NAME);
        return completeEvent;
    }

    /**
     * 获取模版的XML字符串
     *
     * @param process 组装完成的流程节点信息
     * @return XML字符串
     */
    private BpmnModel getModelString(Process process) {
        process.setId(FlowAbleBpmnConstant.getUid());
        BpmnModel bpmnModel = new BpmnModel();
        bpmnModel.addProcess(process);
        // 自动生成布局   布局节点位置
        try {
            new BpmnAutoLayout(bpmnModel).execute();
        } catch (Exception e) {
            log.error("[FlowBpmnServiceImpl].[getModelString] ------> error:", e);
        }
        ProcessValidatorFactory processValidatorFactory = new ProcessValidatorFactory();
        ProcessValidator defaultProcessValidator = processValidatorFactory.createDefaultProcessValidator();
        // 验证失败信息的封装ValidationError
        List<ValidationError> validate = defaultProcessValidator.validate(bpmnModel);
        log.error("获取到的验证信息为：{}", JSONObject.toJSONString(validate));
        return bpmnModel;
    }
}
