package cn.jt.smile.bean.params.instance;

import lombok.Data;

/**
 * @author smile
 */
@Data
public class UpdateTaskUserParams {

    /**
     * 业务数据ID
     */
    private String bizId;

    /**
     * 活跃状态(1-未执行, 2-用户执行, 3-系统执行, 4-系统放弃[挂起等原因])
     */
    private Integer active;
}
