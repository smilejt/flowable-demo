package cn.jt.smile.bean.params.flow;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 审批配置的节点信息(部分参数)
 * @author smile
 */
@Data
public class ApproveNodeParams {

    /**
     * 节点ID
     */
    private String id;

    /**
     * 节点类型 条件 testNode-条件节点(网关之后的连线) normalNode-审批节点(用户任务节点) startNode-开始节点 endNode-结束节点
     */
    private String shape;

    /**
     * 节点名称
     */
    private String label;

    /**
     * 审批权限组
     */
    private List<String> resIds;

    /**
     * 审批节点类型 (1-或签[一票通过], 2-会签[全票通过])
     */
    private Integer nodeType;

    /**
     * 是否抄送(0-否, 1-是)
     */
    private Integer copy = 0;

    /**
     * 抄送用户ID列表
     */
    private List<String> userIds;

    /**
     * 规则名称
     */
    @NotBlank(message = "请输入1-20个字符规则名称")
    @Length(min = 1, max = 20, message = "请输入1-20个字符规则名称")
    private String ruleName;

    /**
     * 规则类型(1-下拉单选择, 2-输入框, 3-责任部门多选)
     */
    @NotBlank(message = "规则类型缺失")
    private Integer paramType;

    /**
     * 规则参数
     */
    @NotBlank(message = "请选择规则参数")
    private String conditionKey;

    /**
     * 规则条件
     */
    @NotBlank(message = "请选择规则条件")
    private String condition;

    /**
     * 条件的判断值
     */
    private String value;
}
