package com.horkr.algorithm.limit_request;

/**
 * @author 卢亮宏
 */
public class TimeWindow {

    private long requestTimes;

    private long startTime;

    public void increase(){
        requestTimes++;
    }

    public long getRequestTimes() {
        return requestTimes;
    }

    public void setRequestTimes(long requestTimes) {
        this.requestTimes = requestTimes;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }


    public TimeWindow() {
    }

    public TimeWindow(long requestTimes, long startTime) {
        this.requestTimes = requestTimes;
        this.startTime = startTime;
    }
}
