package cn.jt.smile.bean.params.flow;

import lombok.Data;

import java.util.List;

/**
 * 流程图对象
 * @author smile
 */
@Data
public class FlowChartParams {

    /**
     * 节点列表
     */
    private List<ApproveNodeParams> nodes;

    /**
     * 连接信息
     */
    private List<ApproveEdgeParams> edges;
}
