package cn.jt.smile.listener;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 会签节点开始时调用的 Listener
 *
 * @author smile
 */
@Component
@Slf4j
public class MultiInstanceStartExecutionListener implements ExecutionListener {
    private static final long serialVersionUID = -3124311419896463037L;
    private static final String UNRELATED_STRING = "unrelated";
    private static final String REJECTED_STRING = "rejected";
    private static final int NUMBER_ZERO = 0;

    @Override
    public void notify(DelegateExecution execution) {
        log.info("[MultiInstanceStartExecutionListener].[notify] ------> execution = {}",
                JSON.toJSONString(ObjectUtils.isEmpty(execution.getId()) ? null : execution.getId()));
        execution.setVariable(UNRELATED_STRING, NUMBER_ZERO);
        execution.setVariable(REJECTED_STRING, NUMBER_ZERO);
    }
}
