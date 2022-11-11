package com.horkr.jdk.learn.jvm.string_table;

//"a"，"b"这种常量是存储在stringTable，stringTable1.6在常量池（方法区），1.7,1.8之后在堆中，方便垃圾回收。
public class Demo {
    public static void main(String[] args) {
        String s1 = "a";  //懒加载的方式，常量池的stringTable没有的话放进去，有的话拿出来.["a"]
        String s2 = "b";//["a","b"]
        String s3 = "a" + "b"; //["a","b","ab"]
        String s4 = s1 + s2; //相当于 new StringBuilder.append(s1).append(s2).toString,创建了新的对象在堆里
        String s5 = "ab";   // ab已在常量池
        String s6 = s4.intern(); // 将s4字符串对象放入常量池，如果有则不会放入，没有放入，会把常量池中的此对象返回

        System.err.println("s3:"+s3);
        System.err.println("s4:"+s4);
        System.err.println("s6:"+s6);

        System.err.println(s3 == s4);  //false
        System.err.println(s3 == s5);  //true
        System.err.println(s3 == s6);  //true

        String x2 = new String("c")+new String("d");
        String x1 = "cd";
        String intern = x2.intern();

        System.err.println(x1==intern); // true
        System.err.println(x1==x2);     // false
    }
}
