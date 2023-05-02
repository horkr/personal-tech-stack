package com.horkr.spring.learn.lock.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class Demo {
    public static ZooKeeper zooKeeper(){
        try {
            return new ZooKeeper("192.168.0.234:2181", 8000, System.err::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 持久监听
     * @throws Exception
     */
    public void listen() throws Exception {
        ZooKeeper zooKeeper = zooKeeper();
        byte[] data = zooKeeper.getData("/llh", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.err.println("listen:"+watchedEvent);
                try {
                    zooKeeper.getData("/llh",this,null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null);
        System.err.println(new String(data));
        System.in.read();
    }



}
