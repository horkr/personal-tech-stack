package com.horkr.jdk.learn.jvm;
/*
 *@Author:lulianghong
 *@Description:
 * java中类装载器把一个类装入JVM，经过以下步骤
 * 1、加载：查找和导入Class文件
 * 2、链接：其中解析步骤是可以选择的
 *      （a）检查：检查载入的class文件数据的正确性
 *      （b）准备：给类的静态变量分配存储空间设置初始值
 *      （c）解析：将常量池中符号引用转成直接引用
 * 3、初始化：对静态变量，静态代码块执行初始化工作
 *
 *
 * 类装载工作由ClassLoder和其子类负责。JVM在运行时会产生三个ClassLoader：
 * BootStrap根装载器，ExtClassLoader(扩展类装载器)和AppClassLoader，其中
 * 根装载器不是ClassLoader的子类，由C++编写，因此在java中看不到他，负责装载JRE的核心类库，如JRE目录下的rt.jar,charsets.jar等。
 * ExtClassLoader是ClassLoder的子类，负责装载JRE扩展目录ext下的jar类包；
 * AppClassLoader负责装载classpath路径下的类包，
 * 这三个类装载器存在父子层级关系****，即根装载器是ExtClassLoader的父装载器，ExtClassLoader是AppClassLoader的父装载器。默认情况下使用AppClassLoader装载应用程序的类
 *
 * 双亲委派模式原理：
 * 加载某个类的class文件时，Java虚拟机采用的是双亲委派模式即把请求交由父类处理，它一种任务委派模式
 * 如果一个类加载器收到了类加载请求，它并不会自己先去加载，而是把这个请求委托给父类的加载器去执行，如果父类加载器还存在其父类加载器，
 * 则进一步向上委托，依次递归，请求最终将到达顶层的启动类加载器，如果父类加载器可以完成类加载任务，就成功返回，倘若父类加载器无法完成此加载任务，
 * 子加载器才会尝试自己去加载，这就是双亲委派模式
 *
 * 双亲委派模式优势：
 * 1.可以避免重复加载，父加载器加载了以后，子加载器没必要再家在一次
 * 2.安全因素，java核心api不会被随意篡改，
 * 举例：假设通过网络传递一个名为java.lang.Integer的类，通过双亲委托模式传递到启动类加载器，而启动类加载器在核心Java API发现这个名字的类，
 * 发现该类已被加载，并不会重新加载网络传递的过来的java.lang.Integer，而直接返回已加载过的Integer.class，这样便可以防止核心API库被随意篡改
 *
 *
 * Java装载类使用“全盘负责委托机制”。
 * “全盘负责”是指当一个ClassLoder装载一个类时，除非显示的使用另外一个ClassLoder，该类所依赖及引用的类也由这个ClassLoder载入；


自定义类加载器的意义：

当class文件不在ClassPath路径下，默认系统类加载器无法找到该class文件，在这种情况下我们需要实现一个自定义的ClassLoader来加载特定路径下的class文件生成class对象。

当一个class文件是通过网络传输并且可能会进行相应的加密操作时，需要先对class文件进行相应的解密后再加载到JVM内存中，这种情况下也需要编写自定义的ClassLoader并实现相应的逻辑。

当需要实现热部署功能时(一个class文件通过不同的类加载器产生不同class对象从而实现热部署功能)，需要实现自定义ClassLoader的逻辑


双亲委派模型的破坏者-线程上下文类加载器:
在Java应用中存在着很多服务提供者接口（Service Provider Interface，SPI），这些接口允许第三方为它们提供实现，如常见的 SPI 有 JDBC、JNDI等，
这些 SPI 的接口属于 Java 核心库，一般存在rt.jar包中，由Bootstrap类加载器加载，而 SPI 的第三方实现代码则是作为Java应用所依赖的 jar 包被存放在classpath路径下，
由于SPI接口中的代码经常需要加载具体的第三方实现类并调用其相关方法，但SPI的核心接口类是由引导类加载器来加载的，而Bootstrap类加载器无法直接加载SPI的实现类，
同时由于双亲委派模式的存在，Bootstrap类加载器也无法反向委托AppClassLoader加载器SPI的实现类。在这种情况下，我们就需要一种特殊的类加载器来加载第三方的类库，
而线程上下文类加载器就是很好的选择。
 *@Date:Created in 10:22 2018/7/7
 */

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class MyClassLoader extends ClassLoader{

//    loadClass()方法是ClassLoader类自己实现的，该方法中的逻辑就是双亲委派模式的实现
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }
//    在JDK1.2之后已不再建议用户去覆盖loadClass()方法，
// 而是建议把自定义的类加载逻辑写在findClass()方法中，从前面的分析可知，findClass()方法是在loadClass()方法中被调用的，
// 当loadClass()方法中父加载器加载失败后，则会调用自己的findClass()方法来完成类加载，这样就可以保证自定义的类加载器也符合双亲委托模式。
// 需要注意的是ClassLoader类中并没有实现findClass()方法的具体代码逻辑，取而代之的是抛出ClassNotFoundException异常
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 获取类的字节数组
        byte[] classData = getClassData();
        if (classData == null) {
            throw new ClassNotFoundException();
        } else {
            //使用defineClass生成class对象
            return defineClass(name, classData, 0, classData.length);
        }
    }

    private byte[] getClassData()  {
        String path = "E:\\Test\\Demo.class";
        try {
            InputStream ins = new FileInputStream(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int bytesNumRead = 0;
            // 读取类文件的字节码
            while ((bytesNumRead = ins.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesNumRead);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        MyClassLoader loader = new MyClassLoader();
        Class<?> demo = loader.loadClass("Demo");
        System.out.println(demo.newInstance().toString());

    }
}
