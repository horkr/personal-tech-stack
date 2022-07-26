package com.horkr.algorithm.verification;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @author 卢亮宏
 */
public class UdpVerify {
    // udp可以随机获取本机端口
    public static void main(String[] args) throws SocketException {
        DatagramSocket udpSocket = new DatagramSocket();
        int localPort = udpSocket.getLocalPort();
        System.out.println(localPort);
    }
}
