package com.horkr.jdk.learn.network.io.bio.chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author 卢亮宏
 */
public class ChatClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("123.60.161.209", 8888);
            System.out.println("Connected to server: " + socket.getInetAddress());

            Thread thread = new Thread(new ServerHandler(socket));
            thread.start();

            OutputStream outputStream = socket.getOutputStream();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                if(message.equals("exit")){
                    socket.close();
                    break;
                }
                outputStream.write(message.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ServerHandler implements Runnable {
        private Socket socket;

        public ServerHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                InputStream inputStream = socket.getInputStream();
                byte[] buffer = new byte[1024];
                while (true) {
                    int len = inputStream.read(buffer);
                    if (len == -1) {
                        break;
                    }
                    String message = new String(buffer, 0, len);
                    System.out.println("Server: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
