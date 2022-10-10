package com.horkr.algorithm.verification;

/**
 * @author 卢亮宏
 */
public class ObjConvertVerify {

    class People{

    }
    public static void main(String[] args) {
        Object obj = null;
        People people = (People) obj;
        System.out.println(people);
    }
}
