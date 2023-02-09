package com.horkr.jdk.learn.network.netty.rpc.serializ;

import java.io.*;

public class ObjSerializer {
    public static byte[] obj2Bytes(Object obj) {

        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            // 为啥一定要关流？底层原因是什么？
            byteArrayOutputStream.close();
            objectOutputStream.close();
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static <T> T bytes2Obj(byte[] bytes, Class<T> tClass) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (T) objectInputStream.readObject();
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
