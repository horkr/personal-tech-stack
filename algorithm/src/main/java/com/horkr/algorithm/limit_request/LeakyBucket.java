package com.horkr.algorithm.limit_request;

import com.horkr.util.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 漏桶限流算法，参考sentinel限流算法的匀速排队算法，RateLimiterController
 *
 * @author 卢亮宏
 */
public class LeakyBucket implements RequestLimit {
    private final Logger log = LoggerFactory.getLogger(LeakyBucket.class);
    /**
     * 上次请求通过时间
     */
    private final AtomicLong latestPassedTime = new AtomicLong(-1);

    /**
     * 每个请求花费的时间
     */
    private final long costTime4perRequest;


    public LeakyBucket(long qps) {
        double qpsDoubleValue = Double.parseDouble(String.valueOf(qps));
        double speed = 1/qpsDoubleValue;
        costTime4perRequest = (long) (speed*1000);
//        costTime4perRequest = Math.round(1.0 * 1 / qps * 1000);
    }


    @Override
    public boolean canPass() {
        boolean pass;
        long needWaitTime = 0;
        long passTime;
        long current = System.currentTimeMillis();
        // 期望请求到来的时间
        long expectRequestTime = latestPassedTime.get() + costTime4perRequest;

        // TODO 此时如果并发来100个请求，是不是都会通过
        if (current >= expectRequestTime) {
            latestPassedTime.set(current);
            pass = true;
            passTime = current;
        } else {
            needWaitTime = expectRequestTime - current;
            if (needWaitTime > 0) {
                ThreadUtil.sleep(TimeUnit.MILLISECONDS, needWaitTime);
            }
            long now = System.currentTimeMillis();
            latestPassedTime.set(now);
            pass = true;
            passTime = now;
        }
        log.info("pass:{},WaitTime:{},expectRequestTime:{},passTime:{}", pass, needWaitTime, expectRequestTime, passTime);
        return pass;
    }
}
