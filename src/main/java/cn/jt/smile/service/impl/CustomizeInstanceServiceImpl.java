package cn.jt.smile.service.impl;

import cn.jt.smile.bean.params.instance.AddInstanceParams;
import cn.jt.smile.bean.params.instance.AddTaskUserParams;
import cn.jt.smile.bean.po.CustomizeInstance;
import cn.jt.smile.mapper.CustomizeInstanceMapper;
import cn.jt.smile.service.CustomizeInstanceService;
import cn.jt.smile.service.FlowApiService;
import cn.jt.smile.service.TaskUserService;
import cn.jt.smile.util.BeanCopy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author smile
 */
@Slf4j
@Service
public class CustomizeInstanceServiceImpl extends ServiceImpl<CustomizeInstanceMapper, CustomizeInstance> implements CustomizeInstanceService {
    private static final String TASK_DEFINITION_KEY = "taskDefinitionKey";

    @Resource
    private FlowApiService apiService;
    @Resource
    private TaskUserService taskUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(AddInstanceParams params) {
        log.info("[CustomizeInstanceServiceImpl].[save] ------> Save Flow Customize Instance Info Start, params = {}", JSON.toJSONString(params));
        CustomizeInstance instance = BeanCopy.copy(params, CustomizeInstance.class);
        instance.setGmtCreate(new Date());
        instance.setGmtModified(new Date());
        if (baseMapper.insert(instance) > 0 && !ObjectUtils.isEmpty(params.getInstanceId())) {
            Map<String, String> activeTask = apiService.getActiveTask(params.getInstanceId());
            if (!activeTask.isEmpty()) {
                String flowNodeId = activeTask.get(TASK_DEFINITION_KEY);
                activeTask.remove(TASK_DEFINITION_KEY);
                List<AddTaskUserParams> insertList = Lists.newArrayList();
                for (String userId : activeTask.keySet()) {
                    AddTaskUserParams param = BeanCopy.copy(params, AddTaskUserParams.class);
                    param.setFlowNodeId(flowNodeId);
                    param.setUserId(userId);
                    param.setTaskId(activeTask.get(userId));
                    insertList.add(param);
                }
                boolean saveResult = taskUserService.batchSave(insertList);
                log.info("[CustomizeInstanceServiceImpl].[save] ------> Batch Save Flow User Task Result = {}", saveResult);
            }
            log.info("[CustomizeInstanceServiceImpl].[save] ------> Save Flow Customize Instance Info End, Save success");
            return true;
        }
        log.info("[CustomizeInstanceServiceImpl].[save] ------> Save Flow Customize Instance Info End, Save Fail");
        return false;
    }
}
