package cn.jt.smile.service;

import cn.jt.smile.bean.params.flow.SaveDeployParams;
import cn.jt.smile.bean.po.CustomizeDeploy;
import cn.jt.smile.bean.vo.CustomizeDeployVO;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * @author smile
 */
public interface CustomizeDeployService extends IService<CustomizeDeploy> {

    /**
     * 保存流程引擎模版
     *
     * @param params 参数对象
     * @return 保存结果
     */
    boolean saveRecord(SaveDeployParams params);

    /**
     * 根据部署ID查询流程信息
     * @param deployId 流程部署ID(非FlowAble流程部署ID)
     * @return 查询结果对象
     */
    CustomizeDeployVO getById(String deployId);

    /**
     * 部署流程定义
     *
     * @param deployId 系统流程定义ID
     * @return 部署结果
     */
    boolean deployFlow(String deployId);
}
