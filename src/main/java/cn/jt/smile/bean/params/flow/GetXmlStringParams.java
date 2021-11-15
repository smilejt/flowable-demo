package cn.jt.smile.bean.params.flow;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author smile
 */
@Data
public class GetXmlStringParams {
    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 前端传入的用户节点对象
     */
    Map<String, ApproveNodeParams> userNodeMap;

    /**
     * 前端传入的条件节点对象
     */
    Map<String, ApproveNodeParams> conditionNodeMap;

    /**
     * 前端传入的连接节点对象
     */
    Map<String, List<ApproveEdgeParams>> edgeMap;

    /**
     * 前端传入的开始节点ID
     */
    String startNodeId;

    /**
     * 前端传入的结束节点ID
     */
    String endNodeId;

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
