package com.horkr.jdk.learn.concurrency._synchronized;

/**
 * synchronized字节码指令  通过javap -c xxx.class命令可查看字节码内容
 *
 * @author 卢亮宏
 */
public class SynchronizedInstruction {
    void sync(){
        synchronized (this){

        }
    }

    public static void main(String[] args) {

    }
}
