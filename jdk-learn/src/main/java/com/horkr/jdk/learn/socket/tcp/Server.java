package com.horkr.jdk.learn.socket.tcp;
/*
 *@Author:lulianghong
 *@Description:此程序作为服务器，浏览器作为客户端
 *@Date:Created in 19:39 2018/7/11
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1111);
            Socket accept = serverSocket.accept();
            InputStream inputStream = accept.getInputStream();
            byte[] buf = new byte[1024];
            int len = inputStream.read(buf);
            String s = new String(buf, 0, len);
            System.out.println(accept.getInetAddress().toString()+":"+s);
            PrintWriter printWriter = new PrintWriter(accept.getOutputStream(),true);
            printWriter.println("<h1>hello world</h1>");
            accept.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
