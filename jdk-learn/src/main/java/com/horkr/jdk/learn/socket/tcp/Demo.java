package com.horkr.jdk.learn.socket.tcp;
/*
 *@Author:lulianghong
 *@Description:
 *@Date:Created in 16:39 2018/7/11
 */

public class Demo {
    public static void main(String[] args) throws Exception {
        TcpSend tcpSend = new TcpSend();
        TcpReceive tcpReceive = new TcpReceive();
        tcpSend.send();
        tcpReceive.receive();
    }
}
