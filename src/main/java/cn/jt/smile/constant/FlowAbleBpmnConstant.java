package cn.jt.smile.constant;

import java.util.UUID;

/**
 * 流程Bpmn20Xml处理常量
 *
 * @author smile
 */
public interface FlowAbleBpmnConstant {

    /**
     * 流程模版定义ID获取
     *
     * @return uid
     */
    static String getUid() {
        return String.format("%s%s", ID_PREFIX_STRING, UUID.randomUUID());
    }

    String ID_PREFIX_STRING = "sia-";

    /**
     * 前端条件节点标记
     */
    String TEST_NODE_MARK = "testNode";

    /**
     * 前端用户节点标记
     */
    String NORMAL_NODE_MARK = "normalNode";

    /**
     * 前端开始节点标记
     */
    String START_NODE_KEY = "startNode";
    /**
     * 开始节点Map存储Key
     */
    String MAP_START_NODE_KEY = "startNodeId";
    /**
     * 前端结束节点标记
     */
    String END_NODE_KEY = "endNode";
    /**
     * 结束节点Map存储Key
     */
    String MAP_END_NODE_KEY = "endNodeId";

    /**
     * 开始节点ID
     */
    String START_EVENT_ID = "start";
    /**
     * 开始节点名称
     */
    String START_EVENT_NAME = "开始";

    /**
     * 完成节点ID
     */
    String COMPLETE_EVENT_ID = "complete";

    /**
     * 完成节点名称
     */
    String COMPLETE_EVENT_NAME = "完成";

    /**
     * 结束节点名称
     */
    String END_EVENT_NAME = "结束";

    /**
     * 结束ID
     */
    String END_EVENT_ID = "end";

    /**
     * 发起连线名称
     */
    String SEQUENCE_NAME_STRING = "发起";

    /**
     * 网关后条件判断参数-审批通过
     */
    String TRUE_APPROVED_STRING = "${approved}";

    /**
     * 网关后条件判断参数-审批失败
     */
    String FALSE_APPROVED_STRING = "${!approved}";

    /**
     * 条件分支格式化模版
     */
    String CONDITION_FORMAT_STRING = "${%s %s %s}";
    String CONDITION_FORMAT_STRING_TWO = "${%s %s}";

    /**
     * 自定义条件
     */
    String CUSTOMIZE_CONDITION_FORMAT_STRING = "${%s}";

    /**
     * 监听器执行类型
     */
    String LISTENER_TYPE = "class";

    /**
     * 用户处理任务监听器class全路径
     */
    String TASK_CLASS_NAME_STRING = "com.zbzk.cmp.workflow.listener.MultiInstanceTaskListener";

    /**
     * 监听器执行开始class全路径
     */
    String START_EXECUTION_CLASS_NAME_STRING = "com.zbzk.cmp.workflow.listener.MultiInstanceStartExecutionListener";

    /**
     * 监听器执行结束class全路径
     */
    String END_EXECUTION_CLASS_NAME_STRING = "com.zbzk.cmp.workflow.listener.MultiInstanceEndExecutionListener";

    /**
     * FLowAble获取审批人EL表达式
     */
    String EL_ASSIGNEE_STRING = "${assignee}";

    /**
     * 审批结束调用方法(组装参数调用自定义方法)
     */
    String CONDITION_START_STRING = "${multiInstanceCompleteTask.accessCondition(execution,";
    String CONDITION_END_STRING = ")}";

    /**
     * 审批该节点的用户组信息(组装参数调用自定义方法)
     */
    String COLLECTION_START_STRING = "${fakeLdapService.findAllSales(\"";
    String COLLECTION_END_STRING = "\")}";
    String CARDINALITY_END_STRING = "\", departmentId)}";

    /**
     * 用户节点循环次数获取方法
     */
    String CARDINALITY_START_STRING = "${fakeLdapService.cycleFrequency(\"";

    /**
     * 审批人参数
     */
    String ASSIGNEE_STRING = "assignee";

    /**
     * 抄送节点名称
     */
    String COPY_NAME_STRING = "抄送";

    /**
     * 抄送执行的表达式
     */
    String COPY_EXPRESSION_NAME_STRING = "${makeCopyTaskEntrust.execute(\"";

    /**
     * ServiceTask的type
     */
    String OPERATION_NAME_STRING = "operation";
}
