package cn.jt.smile.service.impl;

import cn.jt.smile.bean.params.instance.AddTaskUserParams;
import cn.jt.smile.bean.params.instance.QueryTaskUserParams;
import cn.jt.smile.bean.params.instance.UpdateTaskUserParams;
import cn.jt.smile.bean.po.TaskUser;
import cn.jt.smile.bean.vo.TaskUserVO;
import cn.jt.smile.constant.FlowActiveEnum;
import cn.jt.smile.constant.FlowTaskActiveEnum;
import cn.jt.smile.mapper.TaskUserMapper;
import cn.jt.smile.service.TaskUserService;
import cn.jt.smile.util.BeanCopy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author smile
 */
@Slf4j
@Service
public class TaskUserServiceImpl extends ServiceImpl<TaskUserMapper, TaskUser> implements TaskUserService {
    private static final String TASK_ID_KEY = "task_id";
    private static final String DEL_FLAG_KEY = "del_flag";
    private static final String ACTIVE_KEY = "active";
    private static final String FLOW_NODE_ID_KEY = "flow_node_id";

    @Override
    public boolean batchSave(List<AddTaskUserParams> params) {
        log.info("[TaskUserServiceImpl].[batchSave] ------> Insert Flow Active User Task Start, params.size = {}", CollectionUtils.isEmpty(params) ? null : JSON.toJSONString(params));
        if (!CollectionUtils.isEmpty(params)) {
            int insertNumber = 0;
            for (AddTaskUserParams param : params) {
                TaskUser insert = BeanCopy.copy(param, TaskUser.class);
                insert.setDelFlag(false);
                insert.setGmtCreate(new Date());
                insert.setGmtModified(new Date());
                insert.setActive(FlowTaskActiveEnum.NOT_EXECUTION.getCode());
                insertNumber += baseMapper.insert(insert);
            }
            log.info("[TaskUserServiceImpl].[batchSave] ------> Insert Flow Active User Task End, Insert Number = {}, result = true", insertNumber);
            return true;
        }
        log.info("[TaskUserServiceImpl].[batchSave] ------> Insert Flow Active User Task End, Insert Number = 0, result = false");
        return false;
    }

    @Override
    public Map<String, TaskUserVO> getMap(QueryTaskUserParams params) {
        log.info("[TaskUserServiceImpl].[getList] ------> Query Task User List By Task Id Start, params = {}", JSON.toJSONString(params));
        QueryWrapper<TaskUser> qw = new QueryWrapper<>();
        qw.eq(TASK_ID_KEY, params.getTaskId());
        qw.eq(DEL_FLAG_KEY, false);
        qw.eq(ACTIVE_KEY, FlowActiveEnum.NOT_EXECUTION.getCode());
        List<TaskUser> taskUsers = baseMapper.selectList(qw);
        if (CollectionUtils.isEmpty(taskUsers)) {
            throw new RuntimeException("任务ID不存在");
        }
        qw = new QueryWrapper<>();
        qw.eq(DEL_FLAG_KEY, false);
        qw.eq(ACTIVE_KEY, FlowActiveEnum.NOT_EXECUTION.getCode());
        qw.eq(FLOW_NODE_ID_KEY, taskUsers.get(0).getFlowNodeId());
        taskUsers = baseMapper.selectList(qw);
        Map<String, TaskUserVO> resultMap = new ConcurrentHashMap<>(8);
        taskUsers.forEach(task -> resultMap.put(task.getTaskId(), BeanCopy.copy(task, TaskUserVO.class)));
        log.info("[TaskUserServiceImpl].[getList] ------> Query Task User List By Task Id End, resultMap.size = {}", resultMap.size());
        return resultMap;
    }

    @Override
    public boolean batchUpdateActive(List<UpdateTaskUserParams> params) {
        log.info("[TaskUserServiceImpl].[batchUpdateActive] ------> Update Flow Active User Task Start, params.size = {}", CollectionUtils.isEmpty(params) ? null : JSON.toJSONString(params));
        if (!CollectionUtils.isEmpty(params)){
            int updateNum = 0;
            for (UpdateTaskUserParams param : params) {
                TaskUser update = BeanCopy.copy(param, TaskUser.class);
                updateNum += baseMapper.updateById(update);
            }
            log.info("[TaskUserServiceImpl].[batchUpdateActive] ------> Update Flow Active User Task End, Insert Number = {}, result = true", updateNum);
            return true;
        }
        log.info("[TaskUserServiceImpl].[batchUpdateActive] ------> Update Flow Active User Task End, Insert Number = 0, result = false");
        return false;
    }

    @Override
    public boolean checkUserExistTask(String userId) {
        log.info("[TaskUserServiceImpl].[checkUserExistTask] ------> Check User Is Exist Task Start, userId = {}", userId);
        boolean result = false;
        List<TaskUser> taskUsers = baseMapper.selectList(new QueryWrapper<TaskUser>().lambda()
                .eq(TaskUser::getUserId, userId)
                .eq(TaskUser::getDelFlag, false)
                .eq(TaskUser::getActive, FlowTaskActiveEnum.NOT_EXECUTION.getCode()));
        if (!CollectionUtils.isEmpty(taskUsers)){
            result = true;
        }
        log.info("[TaskUserServiceImpl].[checkUserExistTask] ------> Check User Is Exist Task End, result = {}", result);
        return result;
    }
}
