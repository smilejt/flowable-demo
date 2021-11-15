package cn.jt.smile.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;


/**
 * @author admin
 */
@SuppressWarnings("unused")
public class BeanCopy {

    public static <T> T copy(Object o, Class<T> clazz){
        return copy(o, clazz, (String[]) null);
    }

    public static <T> T copy(Object o, Class<T> clazz, String... ignoreProperties){
        T t = BeanUtils.instantiateClass(clazz);
        BeanUtils.copyProperties(o, t, ignoreProperties);
        return t;
    }

    public static <T1,T2>List<T2>  copyBatch(List<T1> objects, Class<T2> clazz){
        return copyBatch(objects, clazz, (String[]) null);
    }

    public static <T1,T2>List<T2>  copyBatch(List<T1> objects, Class<T2> clazz, String... ignoreProperties){
        if(!CollectionUtils.isEmpty(objects)){
            List<T2> res = new ArrayList<>(objects.size());
            for(Object o : objects){
                res.add(copy(o, clazz, ignoreProperties));
            }
            return res;
        }
        return new ArrayList<>();
    }

    /**
     * Copy properties. 复制对象相同属性的属性值
     *
     * @param source the source
     * @param target the target
     */
    public static <T> void copyProperties(Object source, T target) {
        BeanUtils.copyProperties(source, target);
    }

    /**
     * Copy properties t. 复制对象相同属性的属性值 带返回值
     *
     * @param <T>         the type parameter
     * @param source      the source
     * @param targetClass the target class
     * @return the t
     */
    public static <T> T copyProperties(Object source, Supplier<T> targetClass) {
        final T t = targetClass.get();
        BeanUtils.copyProperties(source, t);
        return t;
    }

    /**
     * Copy properties t. 复制对象相同属性的属性值 可配置回调函数
     *
     * @param <S>               the type parameter
     * @param <T>               the type parameter
     * @param source            the source
     * @param target            the target
     * @param beanUtilsCallBack the bean utils call back
     * @return the t
     */
    public static <S, T> T copyProperties(S source, Supplier<T> target, AsciiBeanUtilsCallBack<S, T> beanUtilsCallBack) {
        final T t = target.get();
        BeanUtils.copyProperties(source, t);
        if (beanUtilsCallBack != null) {
            beanUtilsCallBack.callBack(source, t);
        }
        return t;
    }

    /**
     * Copy not null properties. 复制对象相同属性的属性值 忽略null的属性
     *
     * @param source the source
     * @param target the target
     */
    public static void copyNotNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }


    /**
     * Copy list properties list. 复制list
     *
     * @param <S>     the type parameter
     * @param <T>     the type parameter
     * @param sources the sources
     * @param target  the target
     * @return the list
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        List<T> targetList = new ArrayList<>(sources.size());
        sources.forEach(source -> {
            final T t = target.get();
            BeanUtils.copyProperties(source, t);
            targetList.add(t);
        });
        return targetList;
    }

    /**
     * Copy list properties list. 复制list 可配置回调函数
     *
     * @param <S>               the type parameter
     * @param <T>               the type parameter
     * @param sources           the sources
     * @param target            the target
     * @param beanUtilsCallBack the bean utils call back
     * @return the list
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, AsciiBeanUtilsCallBack<S, T> beanUtilsCallBack) {
        List<T> targetList = new ArrayList<>(sources.size());
        sources.forEach(source -> {
            final T t = target.get();
            BeanUtils.copyProperties(source, t);
            if (beanUtilsCallBack != null) {
                beanUtilsCallBack.callBack(source, t);
            }
            targetList.add(t);
        });
        return targetList;
    }

    /**
     * Get null property names string [ ].
     *
     * @param source the source
     * @return the string [ ]
     */
    public static String[] getNullPropertyNames(Object source) {
        final PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(source.getClass());
        //存放对象中属性值为空的属性名
        final HashSet<String> nullProperty = new HashSet<>();
        Arrays.stream(propertyDescriptors).forEach(propertyDescriptor -> {
            try {
                final Object invoke = propertyDescriptor.getReadMethod().invoke(source);
                if (invoke == null) {
                    nullProperty.add(propertyDescriptor.getName());
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
        String[] results = new String[nullProperty.size()];
        return nullProperty.toArray(results);
    }

    /**
     * The interface Ascii bean utils call back.
     *
     * @param <S> the type parameter
     * @param <T> the type parameter
     */
    @FunctionalInterface
    public interface AsciiBeanUtilsCallBack<S, T> {
        /**
         * Call back.
         *
         * @param s the s
         * @param t the t
         */
        void callBack(S s, T t);
    }

    public static <T1, T2> IPage<T2> copyPage(IPage<T1> obj, Class<T2> clazz) {
        if (obj == null) {
            return new Page<>();
        } else {
            IPage<T2> res = new Page<>();
            res.setRecords(copyBatch(obj.getRecords(), clazz));
            res.setTotal(obj.getTotal());
            res.setCurrent(obj.getCurrent());
            res.setSize(obj.getSize());
            return res;
        }
    }

}
