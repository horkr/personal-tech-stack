package com.horkr.jdk.learn.socket.tcp;
/*
 *@Author:lulianghong
 *@Description:
 *@Date:Created in 16:30 2018/7/11
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TcpSend {
    public void send() throws Exception {
        Socket socket = new Socket("127.0.0.1",10001);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("萨瓦迪卡".getBytes());
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int l = inputStream.read(bytes);
        String s = new String(bytes, 0, l);
        System.out.println(s);
        socket.close();
    }

    public static void main(String[] args) throws Exception {
        TcpSend tcpSend = new TcpSend();
        tcpSend.send();
    }
}
