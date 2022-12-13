package com.horkr.jdk.learn.network.io.nio.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class newClient {
    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost",8989));
        socketChannel.configureBlocking(true);
        FileChannel channel = new FileInputStream("D:\\cn_windows_server_2019_x64_dvd_2d80e042.iso").getChannel();
        long l = System.currentTimeMillis();
        long length = channel.transferTo(0, channel.size(),socketChannel);
        System.err.println("字节数:"+length+"  耗时:"+(System.currentTimeMillis()-l));
        channel.close();
    }
}
