package com.horkr.jdk.learn.network.socket.udp;
/*
 *@Author:lulianghong
 *@Description:
 *@Date:Created in 13:31 2018/7/11
 */


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpReceive {
    private static DatagramSocket ds = null;
    private static Integer PORT = 10000;
    UdpReceive() throws SocketException {
        ds = new DatagramSocket(PORT);
    }
    public void receive() throws Exception {
        while (true){
            byte[] data = new byte[1024];
            DatagramPacket serverpacket = new DatagramPacket(data,data.length);
            ds.receive(serverpacket);
            String info = new String(data,0,serverpacket.getLength());
            System.out.println(serverpacket.getAddress().toString()+"----"+serverpacket.getPort()+":"+info);
//            ds.close();
        }
    }

    public static void main(String[] args) throws Exception {
        new UdpReceive().receive();
    }
}
