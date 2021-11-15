package cn.jt.smile.controller;

import cn.jt.smile.bean.params.condition.ConditionQueryParams;
import cn.jt.smile.bean.vo.CustomizeConditionVO;
import cn.jt.smile.service.CustomizeConditionService;
import cn.jt.smile.util.Result;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author smile
 */
@RestController
@RequestMapping("/api/condition")
@Slf4j
public class CustomizeConditionController {

    @Resource
    private CustomizeConditionService service;

    /**
     * 列表查询流程配置可选条件
     *
     * @param params 查询参数
     * @return 查询结果集合
     */
    @SuppressWarnings("unchecked")
    @PostMapping(value = "query/list")
    public Result<List<CustomizeConditionVO>> getList(@Valid @RequestBody ConditionQueryParams params) {
        log.info("[CustomizeConditionController].[getList] ------> Flow Condition Query Start, params = {}", JSON.toJSONString(params));
        List<CustomizeConditionVO> list = service.getList(params);
        log.info("[CustomizeConditionController].[getList] ------> Flow Condition Query End, list.size = {}", CollectionUtils.isEmpty(list) ? null : list.size());
        return Result.success(list);
    }
}
