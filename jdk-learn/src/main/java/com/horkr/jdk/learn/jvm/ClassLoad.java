package com.horkr.jdk.learn.jvm;

/**
 * 参考类加载的顺序，以下的count会怎么输出，如果instance和count的代码顺序调换一下，是否会不一样
 * @author 卢亮宏
 */
public class ClassLoad {

    public static void main(String[] args) {
        System.out.println(Loader.count);
    }

    static class Loader {
        public static Loader instance = new Loader();
        public static int count = 2;

        /**
         * constructure
         */
        private Loader() {
            count++;
        }
    }
}
