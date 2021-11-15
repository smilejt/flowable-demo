package cn.jt.smile.bean.params.flow;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author smile
 */
@Data
public class ApproveConfigParams {

    /**
     * 流程名称
     */
    private String flowName;

    /**
     * 模块单据类型
     */
    private String businessCode;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 流程状态 0-停用 1-启动
     */
    private Boolean flowStats;

    /**
     * 流程描述
     */
    @Length(max = 200, message = "描述信息最多200个字符")
    private String flowDescription;

    /**
     * 同意结果单选: 1-必填, 2-不填, 3-选填
     */
    private Integer sucTitleCode;

    /**
     * 同意结果标题
     */
    private String sucTitle;

    /**
     * 拒绝结果单选: 1-必填, 2-不填, 3-选填
     */
    private Integer faiTitleCode;

    /**
     * 拒绝结果标题
     */
    private String faiTitle;

    /**
     * 流程图信息
     */
    private FlowChartParams flowChart;
}
