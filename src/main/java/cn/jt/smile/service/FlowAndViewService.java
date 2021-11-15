package cn.jt.smile.service;

import cn.jt.smile.bean.params.flow.ViewParams;
import cn.jt.smile.bean.po.FlowAndView;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author smile
 */
public interface FlowAndViewService extends IService<FlowAndView> {

    /**
     * 批量新增前端节点和flowable节点关系数据
     *
     * @param views 参数列表
     * @return 新增结果
     */
    boolean batchSave(List<ViewParams> views);

    /**
     * 根据模板ID查询
     *
     * @param defId 流程部署模板ID
     * @return 查询结果
     */
    Map<String, FlowAndView> getViewByDefId(String defId);
}
