package cn.jt.smile.service.impl;

import cn.jt.smile.bean.params.flow.ViewParams;
import cn.jt.smile.bean.po.FlowAndView;
import cn.jt.smile.mapper.FlowAndViewMapper;
import cn.jt.smile.service.FlowAndViewService;
import cn.jt.smile.util.BeanCopy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author smile
 */
@Slf4j
@Service
public class FlowAndViewServiceImpl extends ServiceImpl<FlowAndViewMapper, FlowAndView> implements FlowAndViewService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSave(List<ViewParams> views) {
        log.info("[FlowAndViewServiceImpl].[save] ------> Flow-View Relation Batch Save Start, views.size = {}", CollectionUtils.isEmpty(views) ? null : views.size());
        if (!CollectionUtils.isEmpty(views)) {
            int insertNumber = 0;
            for (ViewParams param : views) {
                FlowAndView view = BeanCopy.copy(param, FlowAndView.class);
                view.setDelFlag(false);
                insertNumber += baseMapper.insert(view);
            }
            log.info("[FlowAndViewServiceImpl].[save] ------> Flow-View Relation Batch Save End, Insert Number = {}, return = true", insertNumber);
            return true;
        }
        log.info("[FlowAndViewServiceImpl].[save] ------> Flow-View Relation Batch Save End, Insert Number = 0, return = false");
        return false;
    }

    @Override
    public Map<String, FlowAndView> getViewByDefId(String defId) {
        log.info("[FlowAndViewServiceImpl].[getViewByDefId] ------> Get View By Def Id Start, defId = {}", defId);
        List<FlowAndView> flowAndViews = baseMapper.selectList(new QueryWrapper<FlowAndView>().lambda().eq(FlowAndView::getDelFlag, false)
                .eq(FlowAndView::getDefId, defId));
        Map<String, FlowAndView> resultMap = new HashMap<>(CollectionUtils.isEmpty(flowAndViews) ? 1 : flowAndViews.size());
        if (!CollectionUtils.isEmpty(flowAndViews)) {
            flowAndViews.forEach(view -> resultMap.put(view.getFlowNodeId(), view));
        }
        log.info("[FlowAndViewServiceImpl].[getViewByDefId] ------> Get View By Def Id End, flowAndViews.size() = {}", CollectionUtils.isEmpty(flowAndViews) ? null : flowAndViews.size());
        return resultMap;
    }
}
