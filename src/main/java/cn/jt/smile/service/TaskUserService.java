package cn.jt.smile.service;

import cn.jt.smile.bean.params.instance.AddTaskUserParams;
import cn.jt.smile.bean.params.instance.QueryTaskUserParams;
import cn.jt.smile.bean.params.instance.UpdateTaskUserParams;
import cn.jt.smile.bean.po.TaskUser;
import cn.jt.smile.bean.vo.TaskUserVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author smile
 */
public interface TaskUserService extends IService<TaskUser> {

    /**
     * 新增流程执行用户Task关系
     *
     * @param params 新增参数对象
     * @return 新增结果
     */
    boolean batchSave(List<AddTaskUserParams> params);

    /**
     * 查询未执行列表用户任务
     *
     * @param params 查询参数
     * @return 查询结果
     */
    Map<String, TaskUserVO> getMap(QueryTaskUserParams params);

    /**
     * 根据数据ID更新状态
     *
     * @param params 参数对象列表
     * @return 更新结果
     */
    boolean batchUpdateActive(List<UpdateTaskUserParams> params);

    /**
     * 检查用户是否存在任务
     * @param userId 用户ID
     * @return true-存在, false-不存在
     */
    boolean checkUserExistTask(String userId);
}
