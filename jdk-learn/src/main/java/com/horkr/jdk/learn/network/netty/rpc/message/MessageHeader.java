package com.horkr.jdk.learn.network.netty.rpc.message;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * RPC通信消息头
 *
 * @author 卢亮宏
 */
public class MessageHeader implements Serializable {

    private static final long serialVersionUID = -8441979685777852245L;
    /**
     * 标记，可以方状态等信息
     */
    private int flag;

    /**
     * 请求的唯一id
     */
    private long requestId;


    /**
     * 消息体长度
     */
    private long bodyLength;

    @Override
    public String toString() {
       return JSON.toJSONString(this);
    }

    public MessageHeader(int flag, long requestId, long bodyLength) {
        this.flag = flag;
        this.requestId = requestId;
        this.bodyLength = bodyLength;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public long getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(long bodyLength) {
        this.bodyLength = bodyLength;
    }
}
