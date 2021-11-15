package cn.jt.smile.listener;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 抄送实现
 *
 * @author smile
 */
@Component
@Slf4j
public class MakeCopyTaskEntrust {
    /**
     * 抄送调用方法
     *
     * @param businessCode 审批模块的单据类型(配置审批流)
     * @param params       提交审批时的参数(写入审批流程的变量[由变量表达式自动获取])
     * @param dataId       提交审批时的数据ID(写入审批流程的变量[由变量表达式自动获取])
     */
    public void execute(String businessCode, List<String> params, Integer dataId) {
        log.info("[MakeCopyTaskEntrust].[execute] ------>  businessCode = {}, params = {}, dataId = {}", businessCode, JSON.toJSONString(params), dataId);
    }
}
