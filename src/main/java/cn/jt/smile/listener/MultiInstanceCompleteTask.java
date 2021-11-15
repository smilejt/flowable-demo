package cn.jt.smile.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

/**
 * 多实例指定任务类
 *
 * @author smile
 */
@SuppressWarnings("unused")
@Slf4j
@Component
public class MultiInstanceCompleteTask implements Serializable {
    private static final long serialVersionUID = 4161579227624215619L;
    private static final String NR_OF_COMPLETED_INSTANCES_STRING = "nrOfCompletedInstances";
    private static final String NR_OF_INSTANCES_STRING = "nrOfInstances";
    private static final String APPROVED_STRING = "approved";
    private static final int NUMBER_ONE = 1;

    /**
     * 多实例指定任务处理方法
     *
     * @param execution  实例参数
     * @param pastNumber 要求的通过数量
     * @return 处理结果 true-结束会签节点， false-不结束当前会签节点
     */
    public boolean accessCondition(DelegateExecution execution, Integer pastNumber) {

        log.info("[MultiInstanceCompleteTask].[accessCondition] ------> execution = {}", ObjectUtils.isEmpty(execution.getId()) ? null : execution.getId());
        log.info("[MultiInstanceCompleteTask].[accessCondition] ------> pastNumber = {}", pastNumber);

        //已完成的实例数
        int completedInstance = Integer.parseInt(execution.getVariable(NR_OF_COMPLETED_INSTANCES_STRING).toString());
        //会签节点的全部实例数
        int instance = Integer.parseInt(execution.getVariable(NR_OF_INSTANCES_STRING).toString());

        //开始执行判断
        //产品说的，一票通过就是无论同意还是拒绝，只要有一个人审批了就结束! 要是什么时候谁改需求，我骂死谁!【BUG#9472】
        if (instance == completedInstance) {
            //全票通过
            return execution.getTransientVariables().containsKey(APPROVED_STRING) && !Boolean.parseBoolean(execution.getTransientVariables().get(APPROVED_STRING).toString());
        }
        return NUMBER_ONE == pastNumber;
    }
}
