package cn.jt.smile.listener;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 会签节点结束时调用的 Listener
 *
 * @author smile
 */
@Component
@Slf4j
public class MultiInstanceEndExecutionListener implements ExecutionListener {
    private static final long serialVersionUID = 5056711029991557627L;

    @Override
    public void notify(DelegateExecution execution) {
        log.info("[MultiInstanceEndExecutionListener].[notify] ------> execution = {}",
                JSON.toJSONString(ObjectUtils.isEmpty(execution.getId()) ? null : execution.getId()));
    }
}
