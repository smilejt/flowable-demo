package cn.jt.smile.service.impl;

import cn.jt.smile.bean.params.condition.ConditionQueryParams;
import cn.jt.smile.bean.po.CustomizeCondition;
import cn.jt.smile.bean.vo.CustomizeConditionVO;
import cn.jt.smile.constant.ConditionTypeEnum;
import cn.jt.smile.mapper.CustomizeConditionMapper;
import cn.jt.smile.service.CustomizeConditionService;
import cn.jt.smile.util.BeanCopy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * @author smile
 */
@Slf4j
@Service
public class CustomizeConditionServiceImpl extends ServiceImpl<CustomizeConditionMapper, CustomizeCondition> implements CustomizeConditionService {
    private static final String DEL_FLAG_KEY = "del_flag";
    private static final String PRODUCT_ID_KEY = "product_id";
    private static final String TENANT_ID_KEY = "tenant_id";
    private static final String BUSINESS_CODE_KEY = "business_code";

    @Override
    public List<CustomizeConditionVO> getList(ConditionQueryParams params) {
        log.info("[CustomizeConditionServiceImpl].[getList] ------> Get Flow Condition List Start, params = {}", JSON.toJSONString(params));
        QueryWrapper<CustomizeCondition> qw = new QueryWrapper<>();
        qw.eq(DEL_FLAG_KEY, false);
        qw.eq(PRODUCT_ID_KEY, params.getProductId());
        qw.eq(TENANT_ID_KEY, params.getTenantId());
        qw.eq(BUSINESS_CODE_KEY, params.getBusinessCode());
        List<CustomizeCondition> conditions = baseMapper.selectList(qw);
        if (CollectionUtils.isEmpty(conditions)) {
            conditions = Lists.newArrayList();
        }
        List<CustomizeConditionVO> vos = BeanCopy.copyBatch(conditions, CustomizeConditionVO.class);
        Set<String> dicIds = new HashSet<>();
        for (CustomizeConditionVO vo : vos) {
            if (ConditionTypeEnum.DROP_DOWN_LIST.getCode().equals(vo.getParamType())) {
                //单个字典ID不为空
                if (!ObjectUtils.isEmpty(vo.getConditionDictionary())) {
                    dicIds.add(vo.getConditionDictionary());
                }
                //字典列表不为空
                if (!ObjectUtils.isEmpty(vo.getConditionDictionaryList())
                        && CollectionUtils.isEmpty(JSON.parseArray(vo.getConditionDictionaryList(), String.class))) {
                    dicIds.addAll(JSON.parseArray(vo.getConditionDictionaryList(), String.class));
                }
            } else if (ConditionTypeEnum.ORGANIZATION_SELECTION.getCode().equals(vo.getParamType())) {
                //TODO 查询组织信息
                vo.setDicts(new HashMap<>(0));
            }
        }

        //待查询的数据字典不为空
        if (!CollectionUtils.isEmpty(dicIds)) {
            //noinspection MismatchedQueryAndUpdateOfCollection
            Map<String, List<Object>> dicMap = new HashMap<>(8);
            //TODO 查询数据字典并 put 到 dicMap 中
            for (CustomizeConditionVO vo : vos) {
                if (ConditionTypeEnum.DROP_DOWN_LIST.getCode().equals(vo.getParamType())) {
                    List<Object> dict = Lists.newArrayList();
                    vo.setDicts(dict);
                    //单个字典ID不为空
                    if (!ObjectUtils.isEmpty(vo.getConditionDictionary()) && dicMap.containsKey(vo.getConditionDictionary())) {
                        dict.addAll(dicMap.get(vo.getConditionDictionary()));
                    }
                    //字典列表不为空
                    if (!ObjectUtils.isEmpty(vo.getConditionDictionaryList())
                            && CollectionUtils.isEmpty(JSON.parseArray(vo.getConditionDictionaryList(), String.class))) {
                        JSON.parseArray(vo.getConditionDictionaryList(), String.class).forEach(dictCode -> {
                            if (!ObjectUtils.isEmpty(vo.getConditionDictionary()) && dicMap.containsKey(dictCode)) {
                                dict.addAll(dicMap.get(dictCode));
                            }
                        });
                    }
                }
            }
        }
        log.info("[CustomizeConditionServiceImpl].[getList] ------> Get Flow Condition List End, vos.size = {}", vos.size());
        return vos;
    }
}
