package cn.jt.smile.mapper;

import cn.jt.smile.bean.po.CustomizeCondition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author smile
 */
@Mapper
@Repository
public interface CustomizeConditionMapper extends BaseMapper<CustomizeCondition> {
}