package cn.jt.smile.service;

import cn.jt.smile.bean.params.role.BizFlowRoleParams;
import cn.jt.smile.bean.po.BizFlowRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author smile
 */
public interface BizFlowRoleService extends IService<BizFlowRole> {

    /**
     * 保存流程-审批角色关系
     *
     * @param params    参数列表
     * @return 保存结果
     */
    boolean batchSave(List<BizFlowRoleParams> params);
}
