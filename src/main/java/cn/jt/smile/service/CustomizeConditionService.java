package cn.jt.smile.service;

import cn.jt.smile.bean.params.condition.ConditionQueryParams;
import cn.jt.smile.bean.po.CustomizeCondition;
import cn.jt.smile.bean.vo.CustomizeConditionVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author smile
 */
public interface CustomizeConditionService extends IService<CustomizeCondition> {

    /**
     * 查询流程条件列表
     *
     * @param params 查询参数
     * @return 查询结果
     */
    List<CustomizeConditionVO> getList(ConditionQueryParams params);
}
