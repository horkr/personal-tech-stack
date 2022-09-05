package com.horkr.algorithm.verification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author 卢亮宏
 */
public class Stream {

    public static void main(String[] args) {
        stringToIntFlatmap();
    }


    public static void stringToIntFlatmap() {
        List<String> sentences = Arrays.asList("hello world","Jia Gou Wu Dao");
        // 使用流操作
        List<String> results = sentences.stream()
                .flatMap(sentence -> Arrays.stream(sentence.split(" ")))
                .collect(Collectors.toList());
        System.out.println(results);
    }


    static class Student{
        public Student(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        private String name;
        private Integer age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}
