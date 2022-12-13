package com.horkr.jdk.learn.network.io.bio;

import com.horkr.util.compress.CompressUtils;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 单线程处理所有的客户端请求，单个客户端数据处理阻塞，其他客户端则要一直等
 */
public class Server {
    private static Logger logger = LogManager.getLogger(Server.class);

    private static void startServer() {
        ServerSocket serverSocket = null;
        InputStream inputStream = null;
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(8088));
            while (true) {
                Socket socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                String msg = StreamUtils.copyToString(inputStream, Charsets.UTF_8);
                logger.info("接收到消息：{}", msg);
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        startServer();
    }
}
