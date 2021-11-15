package cn.jt.smile.bean.params.role;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author smile
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateAppRoleParams extends AddAppRoleParams{

    /**
     * 数据ID
     */
    @NotNull(message = "数据ID不能为空")
    private String id;
}
