package com.horkr.util.obj;

import cn.hutool.core.util.ReflectUtil;
import com.horkr.util.dto.InconsistentProperty;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * @author 卢亮宏
 */
public class PropertyDiffUtils {


    /**
     * 获取属性差异
     *
     * @param oldObj     原始对象
     * @param newObj     新对象
     * @param propFilter 属性过滤器  通过的断言的将会被对比属性  field:原始对象的field,Object：新对象
     * @return List<InconsistentProperty>
     */
    public static List<InconsistentProperty> obtainDiffProps(Object oldObj, Object newObj, BiPredicate<Field, Object> propFilter) {
        Field[] fields = ReflectUtil.getFields(oldObj.getClass());
        return Arrays.stream(fields).map(field -> {
            if (!propFilter.test(field, newObj)) {
                return null;
            }
            Object oldValue = ReflectUtil.getFieldValue(oldObj, field);
            Object newValue = ReflectUtil.getFieldValue(newObj, field.getName());
            return Objects.equals(oldValue, newValue) ? null : new InconsistentProperty(field.getName(), oldValue, newValue);
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }


    /**
     * 获取属性差异
     *
     * @param oldObj      原始对象
     * @param newObj      新对象
     * @param ignoreProps 忽略的属性
     * @return List<InconsistentProperty>
     */
    public static List<InconsistentProperty> obtainDiffProps(Object oldObj, Object newObj, Set<String> ignoreProps) {
        BiPredicate<Field, Object> propFilter = (field, o) -> !ignoreProps.contains(field.getName());
        return obtainDiffProps(oldObj, newObj, propFilter);
    }


    /**
     * 获取属性差异,排除在新对象上为null的属性
     *
     * @param oldObj             原始对象
     * @param newObj             新对象
     * @return List<InconsistentProperty>
     */
    public static List<InconsistentProperty> obtainDiffPropsIgnoreNull(Object oldObj, Object newObj) {
        BiPredicate<Field, Object> propFilter = (field, o) -> nonNull(ReflectUtil.getFieldValue(o,field));
        return obtainDiffProps(oldObj, newObj, propFilter);
    }
}
