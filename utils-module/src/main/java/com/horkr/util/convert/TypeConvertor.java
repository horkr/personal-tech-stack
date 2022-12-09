package com.horkr.util.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Map;

/**
 * 类型转换器
 *
 * @author 卢亮宏
 */
@Mapper
public interface TypeConvertor {
    TypeConvertor instance = Mappers.getMapper(TypeConvertor.class);

    /**
     * map转对象
     *
     * @param map    map
     * @param tClass 对象
     * @return T
     */
//    <T> T mapToObject(Map<String, Object> map, Class<T> tClass);


}
