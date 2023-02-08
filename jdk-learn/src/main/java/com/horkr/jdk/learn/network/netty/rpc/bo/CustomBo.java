package com.horkr.jdk.learn.network.netty.rpc.bo;

/**
 * 业务数据样例
 */
public class CustomBo {

    private String name;

    private int age;

    public CustomBo(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
