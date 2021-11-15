package cn.jt.smile.mapper;

import cn.jt.smile.bean.po.BizFlowRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author smile
 */
@Mapper
@Repository
public interface BizFlowRoleMapper extends BaseMapper<BizFlowRole> {
}