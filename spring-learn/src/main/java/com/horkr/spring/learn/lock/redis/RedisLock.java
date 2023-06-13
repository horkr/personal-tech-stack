package com.horkr.spring.learn.lock.redis;

import com.horkr.spring.learn.lock.Lock;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RedisLock implements Lock<RedisLockNode> {
    // 在redis对应的setnx的key
    private String name;
    private RedissonClient redissonClient;
    // key中
    private String selfKey;

    public RedisLock(String name) {
        RLock lock = redissonClient.getLock("");
        lock.lock();
        this.name = name;
    }

    /**
     * redis分布式锁的问题
     * 1.加锁后未释放,尽管放在finally里解锁，可能还没执行到finally程序down了.未释放导致其他程序都无法获取锁。所以需要加个超时时间
     * 2.加超时时间加多少合适
     * -多了   加太多时间,导致其他程序白等
     * -少了   业务代码还未执行完就已经被释放了,解锁是删除key，可能吧别人的锁释放了，这一点加一个锁内容判断selfKey，是自己的才删
     * 时间问题，采取续命模式，定时检查锁是否释放，未释放增加超时时间
     *
     * @return boolean
     */
    private boolean tryLock() {
        RBucket<Object> bucket = redissonClient.getBucket(name);
        boolean active = bucket.trySet(selfKey = UUID.randomUUID().toString(), 30, TimeUnit.SECONDS);
        if (active) {
            Thread daemon = new Thread(() -> {
                for (; ; ) {
                    if (selfKey.equals(bucket.get())) {
                        // 检测到锁还在被持有，说明业务代码未执行完，给锁续命
                        bucket.expire(30, TimeUnit.SECONDS);
                        try {
                            TimeUnit.SECONDS.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, "daemon");
            daemon.setDaemon(true);
        }
        return active;
    }

    @Override
    public RedisLockNode lock(Long timeOut) {
        if (tryLock()) {
            return new RedisLockNode(name, selfKey);
        } else {
            //因为这里获取锁失败，应该是要阻塞的，但是无法监控redis锁变化
            return null;
        }
    }



    @Override
    public void unlock(RedisLockNode lockNode) {
        RBucket<Object> bucket = redissonClient.getBucket(name);
        if (bucket.get().equals(selfKey)) {
            bucket.delete();
        }
    }
}
