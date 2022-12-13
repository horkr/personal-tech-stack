package com.horkr.jdk.learn.network.io.nio.zerocopy;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class OldServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8989);
        while (true){
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[4096];
            while (true){
                int read = inputStream.read(bytes);
                if(read==-1){
                    break;
                }
            }
        }
    }
}
