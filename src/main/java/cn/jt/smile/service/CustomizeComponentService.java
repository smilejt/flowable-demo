package cn.jt.smile.service;

import cn.jt.smile.bean.params.component.ComponentQueryParams;
import cn.jt.smile.bean.po.CustomizeComponent;
import cn.jt.smile.bean.vo.CustomizeComponentVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author smile
 */
public interface CustomizeComponentService extends IService<CustomizeComponent> {

    /**
     * 查询前端展示流程组件
     *
     * @param params 查询参数
     * @return 查询结果
     */
    List<CustomizeComponentVO> getComponentByType(ComponentQueryParams params);

    /**
     * 初始化流程组件
     *
     * @param productId 产品ID
     * @param tenantId  租户ID
     * @param initFlows 初始化流程列表
     * @return 初始化结果
     */
    boolean initComponent(String productId, Long tenantId, List<Integer> initFlows);
}
