package com.horkr.util.dto;

/**
 * @author 卢亮宏
 */
public class PairData<K,V> {

    private K key;

    private V value;

    public static <T,U> PairData<T,U> of(T key,U value){
        return new PairData<>(key,value);
    }

    public PairData() {
    }

    public PairData(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
