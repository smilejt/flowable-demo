package cn.jt.smile.bean.po;

import cn.jt.smile.bean.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author smile
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("task_user")
public class TaskUser extends BaseEntity {

    private static final long serialVersionUID = 5397595824761446052L;
    /**
     * 业务数据ID
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
     * 流程实例ID
     */
    private String instanceId;

    /**
     * 流程任务ID
     */
    private String taskId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 流程节点ID
     */
    private String flowNodeId;

    /**
     * 活跃状态(1-未执行, 2-用户执行, 3-系统执行, 4-系统放弃[挂起等原因])
     */
    private Integer active;

    /**
     * 逻辑删除标识(0-否，1-是)
     */
    private Boolean delFlag;
}