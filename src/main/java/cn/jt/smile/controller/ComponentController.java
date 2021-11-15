package cn.jt.smile.controller;

import cn.jt.smile.bean.params.component.ComponentInitParams;
import cn.jt.smile.bean.params.component.ComponentQueryParams;
import cn.jt.smile.bean.vo.CustomizeComponentVO;
import cn.jt.smile.service.CustomizeComponentService;
import cn.jt.smile.util.Result;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author smile
 */
@RestController
@RequestMapping("/api/component")
@Slf4j
public class ComponentController {

    @Resource
    private CustomizeComponentService service;

    @SuppressWarnings("unchecked")
    @PostMapping(value = "query/list")
    public Result<List<CustomizeComponentVO>> getList(@RequestBody ComponentQueryParams params) {
        log.info("[ComponentController].[getList] ------> Customize Component Get List Start, params = {}", JSON.toJSONString(params));
        List<CustomizeComponentVO> result = service.getComponentByType(params);
        log.info("[ComponentController].[getList] ------> Customize Component Get List End");
        return Result.success(result);
    }

    @SuppressWarnings("unchecked")
    @PostMapping(value = "init")
    public Result<Boolean> init(@RequestBody ComponentInitParams params) {
        log.info("[ComponentController].[init] ------> Customize Component Init Start, params = {}", JSON.toJSONString(params));
        boolean result = service.initComponent(params.getProductId(), params.getTenantId(), params.getInitFlows());
        log.info("[ComponentController].[init] ------> Customize Component Init End");
        return Result.success(result);
    }
}
