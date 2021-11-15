package cn.jt.smile.bean.params.instance;

import lombok.Data;

import java.util.Date;

/**
 * @author smile
 */
@Data
public class AddInstanceParams {
    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 数据权限ID
     */
    private String authorityId;

    /**
     * 业务模块编码
     */
    private String businessCode;

    /**
     * 流程定义ID
     */
    private String defId;

    /**
     * 流程实例ID
     */
    private String instanceId;

    /**
     * 启动流程的业务数据ID
     */
    private String dataId;

    /**
     * 提交审批用户ID
     */
    private String putAuditsUserid;

    /**
     * 提交审批时间
     */
    private Date putAuditsTime;

    /**
     * 流程状态
     */
    private Integer flowStatus;

    /**
     * 流程挂起原因
     */
    private String suspendCause;

    /**
     * 备注
     */
    private String remark;
}
