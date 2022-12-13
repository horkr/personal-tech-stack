package com.horkr.jdk.learn.network.io.nio.zerocopy;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class OldClient {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("D:\\cn_windows_server_2019_x64_dvd_2d80e042.iso");
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost",8989));
        OutputStream outputStream = socket.getOutputStream();
        byte[] bytes = new byte[4096];
        long startTime = System.currentTimeMillis();
        int total = 0;
        int readcount = 0;
        while ((readcount = fileInputStream.read(bytes)) > 0) {
            total += readcount;
            outputStream.write(bytes);
        }
        System.err.println("字节数:"+total+"  耗时:"+(System.currentTimeMillis()-startTime));
        fileInputStream.close();
        outputStream.close();
    }
}
