package com.horkr.util.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static java.util.Objects.requireNonNull;

public class SerializerUtil {
    /**
     * 序列化对象
     *
     * @return byte[] 序列化字节数组
     */
    public static byte[] serialize(Object object) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(out)) {
            oos.writeObject(object);
            return out.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("序列化失败", e);
        }
    }

    /**
     * 反序列化对象
     *
     * @param bytes
     *                  字节数组
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] bytes) {
        requireNonNull(bytes, "bytes is null");
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (T) ois.readObject();
        } catch (Exception e) {
            throw new IllegalStateException("反序列化失败", e);
        }
    }
}
