package cn.jt.smile.bean.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.List;


/**
 * @author admin
 */
@SuppressWarnings("all")
@Data
@Accessors(chain = true)
public abstract class AbstractEntity {

    /**
     * 树的子节点
     */
    @TableField(exist = false)
    private List children;

    /**
     * 当前页码
     */
    @TableField(exist = false)
    private long current = 1;

    /**
     * 页面尺寸
     */
    @TableField(exist = false)
    private long size = 10;

    /**
     * 正序排序字段数组
     */
    @TableField(exist = false)
    private String[] ordersASC;

    /**
     * 逆序排序数组
     */
    @TableField(exist = false)
    private String[] ordersDESC;

    /**
     * 获取分页信息
     * @param <T> 类型
     * @return 分页对象
     */
    public <T> Page<T> page(){
        return new Page<T>(this.current, this.size);
    }

    /**
     * 转换为其它实体
     * @param clazz
     * @return 转换后的对象
     */
    public <R> R toElse(Class<R> clazz) {
        return toElse(clazz, (String[]) null);
    }

    /**
     * 转换时忽略字段
     * @param clazz
     * @param ignoreProperties 被忽略的字段名
     * @return 转换后的对象
     */
    public <R> R toElse(Class<R> clazz, String... ignoreProperties) {
        R r = BeanUtils.instantiateClass(clazz);
        if(clazz == null){
            return r;
        }
        BeanUtils.copyProperties(this, r, ignoreProperties);
        return r;
    }

    /**
     * 这个地方用于抽象对象获取参数，由子类去覆盖，如果是其它类型主键，请重载
     */
    public Long getParentId() {
        return 0L;
    }

    /**
     * 这个地方用于抽象对象获取参数，由子类去覆盖，如果是其它类型主键，请重载
     * @return
     */
    public Integer getOrder() {
        return -1;
    }

    public Long getId() {
        return null;
    }
}
