package com.horkr.algorithm.limit_request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;


/**
 * 时间活动窗口限制算法
 *
 * @author 卢亮宏
 */
public class SlidingTimeWindowLimiter implements RequestLimit {
    private final Logger log = LoggerFactory.getLogger(RequestLimit.class);
    private LinkedList<TimeWindow> slots = new LinkedList<>();
    private int slotSize;


    /**
     * 统计次数
     */
    private long statisticsTimes = 0;

    /**
     * 是否限流
     */
    private boolean limit = false;

    /**
     * 限制次数
     */
    private final long limitTimes;

    /**
     * 间隔时间
     */
    private final long interval;

    private long startTime = System.currentTimeMillis();

    public SlidingTimeWindowLimiter(int slotSize, long limitTimes, long interval) {
        this.slotSize = slotSize;
        this.limitTimes = limitTimes;
        this.interval = interval;
    }


    @Override
    public boolean canPass() {
        statisticsTimes++;
        long current = System.currentTimeMillis();
        long timeWindowInterval = interval / slotSize;
        if (current - startTime > timeWindowInterval) {
            TimeWindow last = slots.isEmpty()?new TimeWindow(0,startTime,timeWindowInterval):slots.getLast();
            log.info("已达到一个时间窗口：{}->{},窗口内请求数：{}", startTime, current,  statisticsTimes - last.getCount());
            TimeWindow timeWindow = new TimeWindow(statisticsTimes, startTime, timeWindowInterval);
            slots.add(timeWindow);
            startTime = current;
            if (slots.size() > slotSize) {
                log.info("已超过限定时间窗口数，删除头部窗口");
                slots.removeFirst();
            }
        }
        TimeWindow last = slots.isEmpty()?new TimeWindow(statisticsTimes,startTime,timeWindowInterval):slots.getLast();
        TimeWindow first = slots.size()<=1?new TimeWindow(0,startTime,timeWindowInterval):slots.getFirst();
        long count = statisticsTimes - first.getCount();
        if (count > limitTimes) {
            log.info("时间窗口{}->{}请求数：{}大于限定值：{},不通过", first.getStartTime(), last.getStartTime() + timeWindowInterval, count, limitTimes);
            return false;
        }
        return true;
    }
}
