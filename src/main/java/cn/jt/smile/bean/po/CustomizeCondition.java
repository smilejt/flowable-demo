package cn.jt.smile.bean.po;

import cn.jt.smile.bean.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 条件配置表
 *
 * @author smile
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("customize_condition")
public class CustomizeCondition extends BaseEntity {

    private static final long serialVersionUID = -2226286566503753202L;
    /**
     * 数据业务ID
     */
    private String conditionId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 业务编码
     */
    private String businessCode;

    /**
     * 条件名称(对应下拉选择的显示中文)
     */
    private String conditionName;

    /**
     * 条件key(提交审批表单中的属性字段)
     */
    private String conditionKey;

    /**
     * 条件对于的数据字典
     */
    private String conditionDictionary;

    /**
     * 对应字典ID的JSON列表
     */
    private String conditionDictionaryList;

    /**
     * 条件类型(1-下拉列表，2-数字输入框，3-组织列表)
     */
    private Integer paramType;

    /**
     * 逻辑删除标识(0-否，1-是)
     */
    private Boolean delFlag;
}