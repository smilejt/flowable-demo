package cn.jt.smile.bean.params.flow;

import lombok.Data;

/**
 * 审批配置节点间的连接信息(部分参数)
 * @author smile
 */
@Data
public class ApproveEdgeParams {

    /**
     * 连接信息ID(暂时没用)
     */
    private String id;

    /**
     * 上级节点ID
     */
    private String source;

    /**
     * 下级节点ID
     */
    private String target;
}
