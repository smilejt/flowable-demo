package cn.jt.smile.bean.params.approval;

import lombok.Data;
import org.flowable.bpmn.model.BpmnModel;

/**
 * @author smile
 */
@Data
public class SaveFlowParams {
    /**
     * bpmn 对象
     */
    private BpmnModel bpmnModel;

    /**
     * 流程模版的名称
     */
    private String modelName;
}
