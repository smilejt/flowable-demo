package cn.jt.smile.bean.params.flow;

import lombok.Data;
import org.flowable.bpmn.model.FlowElement;

import java.util.List;
import java.util.Map;

/**
 * @author smile
 */
@Data
public class AssemblyProcessParams {

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 产品ID
     */
    private String productId;
    /**
     * 上一级节点ID(process对象的)
     */
    String lastId;

    /**
     * 下一级节点ID(前端对象的)
     */
    String nextNodeId;

    /**
     * 用户节点(前端的)
     */
    Map<String, ApproveNodeParams> userNodeMap;

    /**
     * 条件节点(前端的)
     */
    Map<String, ApproveNodeParams> conditionNodeMap;

    /**
     * 连接节点(前端的)
     */
    Map<String, List<ApproveEdgeParams>> edgeMap;

    /**
     * 结束节点ID
     */
    String endNodeId;

    /**
     * 结束节点ID(bpmn对象)
     */
    String bpmnEndNodeId;

    /**
     * 连接节点名称(bpmn对象)
     */
    String sequenceName;

    /**
     * 是否第一次调用(反正就是个控制标识)
     */
    int isStart;

    /**
     * 上一级节点对象(bpmn的)
     */
    FlowElement waitNode;

    /**
     * 完成节点ID(bpmn对象)
     */
    String completeNodeId;

    /**
     * 审批模块的业务编码
     */
    String businessCode;

    /**
     * 审批模块的业务名称
     */
    String businessName;

    List<ViewParams> views;
}
