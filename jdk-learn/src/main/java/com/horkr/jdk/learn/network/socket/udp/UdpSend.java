package com.horkr.jdk.learn.network.socket.udp;
/*
 *@Author:lulianghong
 *@Description:
 *@Date:Created in 13:24 2018/7/11
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpSend {
    private static DatagramSocket datagramSocket = null;
    private static String IP = "127.0.0.1";
    private static Integer PORT = 10000;
    UdpSend() throws SocketException {
        datagramSocket = new DatagramSocket();
    }
    public void send() throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String message = null;
        while ((message = bufferedReader.readLine())!=null){
            DatagramPacket datagramPacket = new DatagramPacket(message.getBytes(),message.getBytes().length,InetAddress.getByName(IP),PORT);
            datagramSocket.send(datagramPacket);
        }
//        datagramSocket.close();
    }

    public static void main(String[] args) throws Exception {
        new UdpSend().send();
    }
}
