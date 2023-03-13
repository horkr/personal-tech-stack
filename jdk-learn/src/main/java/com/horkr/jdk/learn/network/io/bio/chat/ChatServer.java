package com.horkr.jdk.learn.network.io.bio.chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ChatServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("Server started on port 9999");

            Socket socket = serverSocket.accept();
            System.out.println("Client connected: " + socket.getInetAddress());

            Thread thread = new Thread(new ClientHandler(socket));
            thread.start();

            OutputStream outputStream = socket.getOutputStream();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                outputStream.write(message.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
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
                    System.out.println("Client: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
