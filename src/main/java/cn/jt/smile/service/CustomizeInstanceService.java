package cn.jt.smile.service;

import cn.jt.smile.bean.params.instance.AddInstanceParams;
import cn.jt.smile.bean.po.CustomizeInstance;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author smile
 */
public interface CustomizeInstanceService extends IService<CustomizeInstance> {

    /**
     * 保存流程实例信息
     *
     * @param params 新增参数
     * @return 保存结果
     */
    boolean save(AddInstanceParams params);
}
