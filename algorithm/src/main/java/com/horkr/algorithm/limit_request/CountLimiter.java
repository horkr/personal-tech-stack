package com.horkr.algorithm.limit_request;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 计数器限流法
 *
 * @author 卢亮宏
 */
public class CountLimiter implements RequestLimit{
    private final Logger log = LoggerFactory.getLogger(CountLimiter.class);
    /**
     * 开始时间
     */
    private long startTime = System.currentTimeMillis();

    /**
     * 统计次数
     */
    private long statisticsTimes = 0;

    /**
     * 限制次数
     */
    private final long limitTimes;

    /**
     * 间隔时间
     */
    private final long interval;


    public CountLimiter(long limitTimes, long interval) {
        this.limitTimes = limitTimes;
        this.interval = interval;
    }


    /**
     * 是否通过校验
     *
     * @return boolean
     */
    public boolean canPass() {
        long current = System.currentTimeMillis();
        // 如果还在时间窗口内，对比统计次数和限制次数
        if (current < startTime + interval) {
            statisticsTimes++;
            log.info("当前时间窗口计数：{},限制数：{}", statisticsTimes, limitTimes);
            return statisticsTimes <= limitTimes;
        } else {
            startTime = current;
            statisticsTimes = 1;
            log.info("已进入新的时间窗口,计数重置");
            return true;
        }
    }
}
