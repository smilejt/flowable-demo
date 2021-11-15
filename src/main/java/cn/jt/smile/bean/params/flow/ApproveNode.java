package cn.jt.smile.bean.params.flow;

import lombok.Data;

import java.util.List;

/**
 * @author smile
 */
@Data
public class ApproveNode {
    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点类型(1-或签, 2-会签)
     */
    private Integer nodeType;

    /**
     * 审批用户组ID列表
     */
    private List<String> resIds;

    /**
     * 是否抄送(0-否, 1-是)
     */
    private Integer copy = 0;

    /**
     * 抄送用户ID列表
     */
    private List<Long> userIds;
}
