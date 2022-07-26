package com.horkr.util;

import com.horkr.util.dto.ListCompareResult;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * 集合通用工具类
 *
 * @author llh
 */
public class CollectionCommonUtils {

    /**
     * 对比两个list，，获取新增、删除、以及无变化的list
     *
     * @param oldList oldList
     * @param newList newList
     * @param <T>     <T>
     * @return ListCompareResult<T>
     * @author llh
     */
    public static <T extends Comparable<T>> ListCompareResult<T> compareList(List<T> oldList, List<T> newList) {
        oldList = isNull(oldList) ? Collections.emptyList() : oldList;
        newList = isNull(newList) ? Collections.emptyList() : newList;
        List<T> addList = new ArrayList<>();
        List<T> removeList = new ArrayList<>();
        List<T> noChangeList = new ArrayList<>();
        List<T> changeList = new ArrayList<>();
        List<T> replaceList = new ArrayList<>();
        oldList.sort(Comparable::compareTo);
        newList.sort(Comparable::compareTo);
        int oldDataIndex = 0;
        int newDataIndex = 0;
        while (oldDataIndex < oldList.size() && newDataIndex < newList.size()) {
            T oldItem = oldList.get(oldDataIndex);
            T newItem = newList.get(newDataIndex);
            int compareResult = newItem.compareTo(oldItem);
            if (compareResult == 0) {
                noChangeList.add(oldItem);
                oldDataIndex++;
                newDataIndex++;
            } else if (compareResult < 0) {
                addList.add(newItem);
                newDataIndex++;
            } else {
                removeList.add(oldItem);
                oldDataIndex++;
            }
        }
        for (; oldDataIndex < oldList.size(); oldDataIndex++) {
            removeList.add(oldList.get(oldDataIndex));
        }
        for (; newDataIndex < newList.size(); newDataIndex++) {
            addList.add(newList.get(newDataIndex));
        }
        return new ListCompareResult<>(addList, removeList, noChangeList, changeList, replaceList);
    }

    /**
     * 获取list第一个元素
     *
     * @param list list
     * @return T
     */
    public static <T> T getFirst(List<T> list) {
        return CollectionUtils.isEmpty(list)?null:list.get(0);
    }


    /**
     * 将集合分组成map，同样的key将置于同一个list中
     *
     * @param collection     collection
     * @param keyConvertor   key的转换器
     * @param valueConvertor vaule的转换器
     * @return Map<K, List < V>>
     */
    public static <K, V, T> Map<K, List<V>> groupCollection(Collection<T> collection, Function<T, K> keyConvertor, Function<T, V> valueConvertor) {
        Map<K, List<V>> result = new HashMap<>();
        for (T item : collection) {
            K key = keyConvertor.apply(item);
            List<V> list = result.get(key);
            if (isNull(list)) {
                list = new ArrayList<>();
                result.put(key, list);
            }
            list.add(valueConvertor.apply(item));
        }
        return result;
    }

    /**
     * 将集合转换为map（Stream转换时重复key会报错）
     *
     * @param collection     collection
     * @param keyConvertor   key的转换器
     * @param valueConvertor vaule的转换器
     * @param <K>            key的数据类型
     * @param <V>            value的数据类型
     * @param <T>            集合元素的数据类型
     * @return Map<K, V>
     * @author llh
     */
    public static <K, V, T> Map<K, V> convertCollection2Map(Collection<T> collection, Function<T, K> keyConvertor, Function<T, V> valueConvertor) {
        return convertCollection2Map(collection, keyConvertor, valueConvertor, true);
    }

    /**
     * 将集合转换为map（Stream转换时重复key会报错）
     *
     * @param collection       collection
     * @param keyConvertor     key的转换器
     * @param valueConvertor   vaule的转换器
     * @param allowRepeatedKey 是否允许出现同样的key
     * @param <K>              key的数据类型
     * @param <V>              value的数据类型
     * @param <T>              集合元素的数据类型
     * @return Map<K, V>
     * @author llh
     */
    public static <K, V, T> Map<K, V> convertCollection2Map(Collection<T> collection, Function<T, K> keyConvertor, Function<T, V> valueConvertor, boolean allowRepeatedKey) {
        Map<K, V> map = new HashMap<>();
        for (T t : collection) {
            K key = keyConvertor.apply(t);
            if (!allowRepeatedKey && nonNull(map.get(key))) {
                throw new IllegalStateException("key is repeated:" + key);
            }
            map.put(key, valueConvertor.apply(t));
        }
        return map;
    }

    public static <T> void consumeIfNotEmpty(Collection<T> collection, Consumer<T> consumer) {
        if (CollectionUtils.isEmpty(collection)) {
            return;
        }
        collection.forEach(consumer);
    }

    /**
     * 如果原始不为空，添加元素到集合中
     *
     * @param collection 集合
     * @param item       新元素
     * @param <T>        T
     */
    public static <T> void addIfNoNull(Collection<T> collection, T item) {
        if (nonNull(item)) {
            collection.add(item);
        }
    }
}
