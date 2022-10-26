package com.horkr.algorithm.verification;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * @author 卢亮宏
 */
public class SplitVerify {

    private static final String PROP_DELIMITER = ",";
    private static final String SAME_PROP_CONCAT = "*";
    public static void main(String[] args) {
        System.out.println(test("",""));
    }


    private static String test( String existProp,String newProp){
        if (isNull(newProp)) {
            return existProp;
        }
        if (isNull(existProp)) {
            return StringUtils.join(newProp, SAME_PROP_CONCAT, 1);
        }
        String[] existPropArr = existProp.split(PROP_DELIMITER);
        List<String> existPropList = new ArrayList<>(Arrays.asList(existPropArr));
        // 查找已存在的属性
        Pair<Integer, Integer> position = findExistProp(existPropList, newProp);
        // 拼接属性
        return concatProp(existPropList, position.getLeft(), position.getRight(), newProp);
    }



    /**
     * 拼接属性
     *
     * @param existPropList 源属性
     * @param samePropSize  已存在属性的数量
     * @param index         已存在属性的位置
     * @param newProp       新属性
     * @return String
     */
    private static String concatProp(List<String> existPropList, int samePropSize, int index, String newProp) {
        if (samePropSize == 0) {
            existPropList.add(StringUtils.join(newProp, SAME_PROP_CONCAT, 1));
        } else {
            existPropList.set(index, StringUtils.join(newProp, SAME_PROP_CONCAT, samePropSize));
        }
        return String.join(PROP_DELIMITER, existPropList);
    }

    /**
     * 查找已存在的属性，实例如下
     * 已存在属性  [a*2,b*3,c*4]新属性为c,那么返回的结果中，已存在属性数量=4，已存在属性位置=2
     *
     * @param existPropList 源属性
     * @param newProp       新属性
     * @return Pair<Integer, Integer> <已存在属性的数量,已存在属性的位置></>
     */
    private static Pair<Integer, Integer> findExistProp(List<String> existPropList, String newProp) {

        // 已存在属性的数量
        int samePropSize = 0;
        // 已存在属性的位置
        int index = -1;
        for (int i = 0; i < existPropList.size(); i++) {
            String existSingleValue = existPropList.get(i);
            if (existSingleValue.equals(newProp)) {
                String[] split = existSingleValue.split("\\*");
                samePropSize = split.length == 1 ? 1 : Integer.parseInt(split[1]);
                samePropSize++;
                index = i;
                break;
            }
        }
        return Pair.of(samePropSize, index);
    }
}
