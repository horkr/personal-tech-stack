package com.horkr.algorithm.limit_request;

/**
 * @author 卢亮宏
 */
public class TimeWindow {

    private long count;

    private long startTime;

    private long interval;

    public void increase(){
        count++;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public TimeWindow() {
    }

    public TimeWindow(long count, long startTime, long interval) {
        this.count = count;
        this.startTime = startTime;
        this.interval = interval;
    }


}
