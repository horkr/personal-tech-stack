package com.horkr.jdk.learn.socket.udp;
/*
 *@Author:lulianghong
 *@Description:
 *@Date:Created in 15:16 2018/7/11
 */

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Receive implements Runnable {
    private DatagramSocket ds;
    public Receive(DatagramSocket ds){
        this.ds = ds;
    }
    @Override
    public void run() {
        try{
        while (true){
            byte[] data = new byte[1024];
            DatagramPacket serverpacket = new DatagramPacket(data,data.length);
            ds.receive(serverpacket);
            String info = new String(data,0,serverpacket.getLength());
            System.out.println(serverpacket.getAddress().toString()+"----"+serverpacket.getPort()+":"+info);
        }
        }
        catch (Exception e){
                    e.printStackTrace();
        }
    }

}
