package cn.jt.smile.bean.params.approval;

import lombok.Data;

/**
 * @author admin
 */
@Data
public class CompleteTaskParams {
    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务执行结果(true-通过， false-拒绝)
     */
    private boolean approved;

    /**
     * 执行结果描述
     */
    private String comments;
}
