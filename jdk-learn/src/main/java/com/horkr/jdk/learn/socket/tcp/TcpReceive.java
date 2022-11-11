package com.horkr.jdk.learn.socket.tcp;
/*
 *@Author:lulianghong
 *@Description:
 *@Date:Created in 16:32 2018/7/11
 */


import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpReceive {
    public void receive() throws Exception {
        ServerSocket serverSocket = new ServerSocket(10001);
        Socket accept = serverSocket.accept();
        InputStream inputStream = accept.getInputStream();
        byte[] buf = new byte[1024];
        int len = inputStream.read(buf);
        String s = new String(buf, 0, len);
        System.out.println(accept.getInetAddress().toString()+":"+s);
        OutputStream outputStream = accept.getOutputStream();
        outputStream.write("我收到了".getBytes());
        accept.close();
        serverSocket.close();
    }

    public static void main(String[] args) throws Exception {
        TcpReceive tcpReceive = new TcpReceive();
        tcpReceive.receive();
    }
}
