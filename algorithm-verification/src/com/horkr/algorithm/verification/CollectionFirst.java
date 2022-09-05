package com.horkr.algorithm.verification;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 卢亮宏
 */
public class CollectionFirst {
    public static void main(String[] args) {
        List<String> objects = new ArrayList<>();
        objects.add("2222");
        objects.add("3333");
        String aDefault = objects.stream().findFirst().orElse("default");
        System.out.println(aDefault);
    }
}
