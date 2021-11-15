package cn.jt.smile.bean.params.condition;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author smile
 */
@Data
public class ConditionQueryParams {

    /**
     * 租户ID
     */
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    /**
     * 产品ID
     */
    @NotBlank(message = "产品ID不能为空")
    private String productId;

    /**
     * 业务编码
     */
    @NotBlank(message = "业务模块编码不能为空")
    private String businessCode;
}
