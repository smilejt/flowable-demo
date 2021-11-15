package cn.jt.smile.listener;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

/**
 * 会签节点每个Activity的complete执行完成后都会回调此方法
 *
 * @author smile
 */
@Component
@Slf4j
public class MultiInstanceTaskListener implements TaskListener {

    private static final long serialVersionUID = -5645036734503266140L;
    private static final String REJECTED_STRING = "rejected";
    private static final String REJECT_STRING = "reject";
    private static final String APPROVED_STRING = "approved";

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("[MultiInstanceTaskListener].[notify] ------> delegateTask = {}", JSON.toJSONString(delegateTask));
        //result的值为控制类中taskService.complete(taskId, map)时,map中所设
        String result = delegateTask.getVariable(APPROVED_STRING).toString();
        //ExecutionListener类中设置的拒绝计数变量
        int rejectedCount = (int) delegateTask.getVariable(REJECTED_STRING);
        if (REJECT_STRING.equals(result)) {
            //拒绝
            delegateTask.setVariable(REJECTED_STRING, ++rejectedCount);
        }
    }
}
