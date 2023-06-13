package com.horkr.spring.learn.lock.zk;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * zk分布式锁实现
 */
public class ZkLock{
    private final String server = "192.168.0.234:2181";
    private final ZkClient zkClient;
    private String lockName;

    public ZkLock(String lockName) {
        zkClient = new ZkClient(server, 5000, 20000);
        // 创建根节点
        buildRoot(lockName);
    }

    // 构建根节点
    public void buildRoot(String lockName) {
        this.lockName = "/"+lockName;
        if (!zkClient.exists(this.lockName)) {
            zkClient.createPersistent(this.lockName);
        }
    }

    /**
     * 加锁
     * @param timeout   timeout
     * @return  lock.Lock
     */
    public LockNode lock(Long timeout) {
        // 创建锁节点（临时序号节点）
        LockNode lockNode = newLockNode();
        // 尝试激活锁
        tryActiveLock(lockNode);
        // 当激活失败后会wait,为前一节点增加删除监听事件，前一节点被删除后再尝试激活锁，激活成功便获取到锁
        if (!lockNode.isActive()) {
            try {
                synchronized (lockNode) {
                    if(nonNull(timeout)){
                        lockNode.wait(timeout);
                    }else {
                        lockNode.wait();
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if(nonNull(timeout)&&!lockNode.isActive()){
            throw new RuntimeException("lock timeout");
        }
        String name = Thread.currentThread().getName();
        System.out.println(String.format("当前线程：%s,获取锁成功", name));
        return lockNode;
    }

    /**
     * 解锁即删除前驱临时序号节点
     * @param lock  lock
     */
    public void unlock(LockNode lock) {
        if (lock.isActive()) {
            zkClient.delete(lock.getPath());
            String name = Thread.currentThread().getName();
            System.out.println(String.format("当前线程：%s,释放锁成功", name));
        }
    }

    // 尝试激活锁
    private LockNode tryActiveLock(LockNode lockNode) {
        // 判断当前是否为最小节点
        List<String> list = zkClient.getChildren(lockName)
                .stream()
                .sorted()
                .map(p -> lockName +"/"+  p)
                .collect(Collectors.toList());
        // 获取最小节点
        String minNodePath = list.get(0);
        // 如果自己是最小节点，激活锁成功
        if (minNodePath.equals(lockNode.getPath())) {
            lockNode.setActive(true);
        } else {
            /**
             * 自身节点前边还有节点
             * 监听前一节点的删除动作,前驱节点被删除后，再次尝试获取锁，成功了边唤醒自己。
             */
            String preNodePath = list.get(list.indexOf(lockNode.getPath()) - 1);
            zkClient.subscribeDataChanges(preNodePath, new IZkDataListener() {
                @Override
                public void handleDataChange(String dataPath, Object data) throws Exception {}
                @Override
                public void handleDataDeleted(String dataPath) throws Exception {
                    // 事件处理 与心跳 在同一个线程，如果Debug时占用太多时间，将导致本节点被删除，从而影响锁逻辑。
                    System.out.println("前一锁节点已被删除:" + dataPath);
                    LockNode lock = tryActiveLock(lockNode);
                    synchronized (lockNode) {
                        if (lock.isActive()) {
                            lockNode.notify();
                        }
                    }
                    zkClient.unsubscribeDataChanges(preNodePath, this);
                }
            });
        }
        return lockNode;
    }


    /**
     * 创建临时序号节点
     * @return  lock.Lock
     */
    public LockNode newLockNode() {
        String nodePath = zkClient.createEphemeralSequential(String.format("%s%s", lockName, lockName), "w");
        return new LockNode(nodePath);
    }
}