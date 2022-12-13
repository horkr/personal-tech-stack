package com.horkr.jdk.learn.network.socket.udp;
/*
 *@Author:lulianghong
 *@Description:
 *@Date:Created in 15:13 2018/7/11
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Send implements Runnable {
    private DatagramSocket ds;
    public Send(DatagramSocket ds){
        this.ds = ds;
    }
    @Override
    public void run() {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String message = null;
        try {
            while ((message = bufferedReader.readLine())!=null){
                DatagramPacket datagramPacket = new DatagramPacket(message.getBytes(),message.getBytes().length,InetAddress.getByName("127.0.0.1"),10000);
                ds.send(datagramPacket);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
