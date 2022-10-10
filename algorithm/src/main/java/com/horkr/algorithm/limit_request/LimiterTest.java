package com.horkr.algorithm.limit_request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 卢亮宏
 */
public class LimiterTest {
    private static final Logger log = LoggerFactory.getLogger(LimiterTest.class);
    private static RequestLimit limiter = new SlidingTimeWindowLimiter(6,100, 1000);


    public static void main(String[] args) {
        long time = 1000;
        long count = 151;
        for (int i = 0; i < count; i++) {
            if (!limiter.canPass()) {
                log.info("block");
            }else {
                log.info("pass");
            }
            try {
                Thread.sleep(time/count);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
