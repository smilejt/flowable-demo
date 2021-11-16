package cn.jt.smile.bean.po;

import cn.jt.smile.bean.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 流程实例表
 *
 * @author smile
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("customize_instance")
public class CustomizeInstance extends BaseEntity {

    private static final long serialVersionUID = -1428897937656580129L;
    /**
     * 数据业务ID
     */
    private String bizId;

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
     * 当前任务ID的JSON数组
     */
    private String taskIdList;

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

    /**
     * 逻辑删除标识(0-否，1-是)
     */
    private Boolean delFlag;
}