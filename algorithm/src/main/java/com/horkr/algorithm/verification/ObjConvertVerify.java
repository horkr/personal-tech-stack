package com.horkr.algorithm.verification;

/**
 * 对null进行类型转换时不会报错
 *
 * @author 卢亮宏
 */
public class ObjConvertVerify {

    class People {

    }

    public static void main(String[] args) {
        Object obj = null;
        People people = (People) obj;
        System.out.println(people);
    }
}
