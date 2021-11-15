package cn.jt.smile.service;

import cn.jt.smile.bean.params.flow.ApproveEdgeParams;
import cn.jt.smile.bean.params.flow.ApproveNodeParams;
import cn.jt.smile.bean.params.flow.ViewParams;
import org.flowable.bpmn.model.BpmnModel;

import java.util.List;

/**
 * @author smile
 */
public interface FlowBpmnService {

    /**
     * 创建审批流的Bpmn
     *
     * @param nodes         节点参数
     * @param edges         连接参数
     * @param documentation 审批结果标题
     * @param businessCode  业务流程编码
     * @param businessName  业务流程名称
     * @param tenantId      租户ID
     * @param productId     产品ID
     * @param views         关联数据参数列表
     * @return 创建后的 BpmnModel 对象
     */
    BpmnModel createFlowBpmn(List<ApproveNodeParams> nodes, List<ApproveEdgeParams> edges, String documentation, String businessCode,
                             String businessName, Long tenantId, String productId, List<ViewParams> views);
}
