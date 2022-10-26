package com.horkr.algorithm.limit_request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;


/**
 * 时间活动窗口限制算法
 * 在我们的例子中，一个时间窗口就是一分钟。然后我们将时间窗口进行划分，比如图中，我们就将滑动窗口划成了6格，所以每格代表的是10秒钟。每过10秒钟，我们的时间窗口就会往右滑动一格。每一个格子都有自己独立的计数器counter，比如当一个请求 在0:35秒的时候到达，那么0:30~0:39对应的counter就会加1。计算某个时间点请求是否超过限流阈值，其实就是将当前时间往前推一个单位时间并计算单位时间的请求数
 * 计数器算法其实就是滑动窗口算法。只是它没有对时间窗口做进一步地划分，所以只有1格。
 * 由此可见，当滑动窗口的格子划分的越多，那么滑动窗口的滚动就越平滑，限流的统计就会越精确。
 *
 * @author 卢亮宏
 */
public class SlidingTimeWindowLimiter implements RequestLimit {
    private final Logger log = LoggerFactory.getLogger(RequestLimit.class);
    private final LinkedList<TimeWindow> slots = new LinkedList<>();
    private final int slotSize;


    /**
     * 请求次数
     */
    private long requestTimes = 0;


    /**
     * 限制次数
     */
    private final long limitRequestTimes;


    /**
     * 时间窗口间隔时间
     */
    private final long timeWindowInterval;

    /**
     * 最新时间窗口开始时间
     */
    private long lastTimeWindowStartTime = System.currentTimeMillis();

    public SlidingTimeWindowLimiter(int slotSize, long limitRequestTimes, long interval) {
        this.slotSize = slotSize;
        this.limitRequestTimes = limitRequestTimes;
        // 限制的单位时间，比如 1分钟限制只能访问
        this.timeWindowInterval = interval / this.slotSize;
        autoSliding();
    }

    /**
     * 滑动时间窗口限流实现
     * 假设某个服务最多只能每秒钟处理100个请求，我们可以设置一个1秒钟的滑动时间窗口，
     * 窗口中有10个格子，每个格子100毫秒，每100毫秒移动一次，每次移动都需要记录当前服务请求的次数
     */
    @Override
    public boolean canPass() {
        requestTimes++;
        long requestTimesInUnitTime = computeRequestTimesInUnitTime();
        printLog();
        return requestTimesInUnitTime <= limitRequestTimes;
    }

    /**
     *
     * 单个线程让时间窗一直往前走
     *
     */
    private void autoSliding() {
        new Thread(() -> {
            while (true) {
                long currentTime = System.currentTimeMillis();
                if (reachNewTimeWindow(currentTime)) {
                    // 创建新的时间窗口
                    createNewTimeWindow(currentTime);
                    // 当时间窗口满了，删除第一个
                    removeFirstTimeWindowIfSlotFull();
                    // 创建完一个时间窗格后，理论上在timeWindowInterval内不需要再检查if 条件，所以可以休眠，减少cpu占用
                    try {
                        Thread.sleep(timeWindowInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    private static final DateFormat FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");

    private void printLog() {
        if (slots.isEmpty()) {
            log.info("当前没有时间窗口...");
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("各时间窗口统计：");
        for (TimeWindow slot : slots) {
            builder.append(FORMAT.format(slot.getStartTime()))
                    .append("-")
                    .append(FORMAT.format(slot.getStartTime() + timeWindowInterval))
                    .append("-->")
                    .append(slot.getRequestTimes())
                    .append(";");
        }
        log.info(builder.toString());
        long requestTimesInUnitTime = computeRequestTimesInUnitTime();
        TimeWindow first = slots.getFirst();
        log.info("时间窗口{}->{}请求数：{}", first.getStartTime(), System.currentTimeMillis(), requestTimesInUnitTime);
    }


    /**
     * 计算单位时间请求次数
     *
     * @return long
     */
    private long computeRequestTimesInUnitTime() {
        TimeWindow first = slots.size() <= 1 ? new TimeWindow(0, lastTimeWindowStartTime) : slots.getFirst();
        return requestTimes - first.getRequestTimes();
    }

    /**
     * 创建新的时间窗口
     *
     * @param currentTime 当前时间
     */
    private void createNewTimeWindow(long currentTime) {
        TimeWindow timeWindow = new TimeWindow(requestTimes, lastTimeWindowStartTime);
        slots.add(timeWindow);
        lastTimeWindowStartTime = currentTime;
    }

    /**
     * 当时间窗口满了，删除第一个
     */
    private void removeFirstTimeWindowIfSlotFull() {
        if (slots.size() > slotSize) {
            slots.removeFirst();
        }
    }


    /**
     * 判断是否到达了新的时间窗口
     *
     * @param currentTime 当前时间
     * @return boolean
     */
    private boolean reachNewTimeWindow(long currentTime) {
        return currentTime - lastTimeWindowStartTime > timeWindowInterval;
    }
}
