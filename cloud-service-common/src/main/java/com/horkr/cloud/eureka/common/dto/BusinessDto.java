package com.horkr.cloud.eureka.common.dto;

import org.springframework.stereotype.Component;

/**
 * @author 卢亮宏
 */
public class BusinessDto {

    public BusinessDto(String name, String age) {
        this.name = name;
        this.age = age;
    }

    private String name;

    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }


    @Override
    public String toString() {
        return "BusinessDto{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
