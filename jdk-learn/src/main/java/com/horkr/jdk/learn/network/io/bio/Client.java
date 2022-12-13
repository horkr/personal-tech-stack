package com.horkr.jdk.learn.network.io.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    private static void clientConnect() {
        for (int i = 0; i < 10; i++) {
            Socket socket = new Socket();
            try {
                socket.connect(new InetSocketAddress(8088));
                OutputStream outputStream = socket.getOutputStream();
                String msg = "From:client " + i + ",Message:hello";
                outputStream.write(msg.getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        clientConnect();
    }
}
