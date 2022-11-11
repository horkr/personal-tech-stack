package com.horkr.jdk.learn.socket;
/*
 *@Author:lulianghong
 *@Description:
 * UDP:1.面向无连接
 *     2.数据会被封包（每个数据包最多传输64k数据，多的则分开发）
 *     3.是不可靠协议（因为是无连接）
 *     4.速度快
 * 举例：视频会议，桌面共享
 *
 * TCP：1.面向连接，建立通道
 *      2.在连接中可进行大量数据传递
 *      3.三次握手，可靠
 *      4.必须建立连接，效率低
 *
 *
 *  UDP 主要对象就是DatagramSocket（Socket端口），DatagramPackage数据包
 *  用多线程创建一个发送线程，一个接受线程，然后俩个线程一起运行
 *
 *
 *  TCP建立连接后客户端和服务端都用客户端的流对象,主要对象Socket（发送端，客户端），ServerSocket（接收端，服务端）
 *
 *  tomcat底层封装的是ServerSocket
 *
 *  http是基于请求和响应模式的无连接，无状态的应用层协议，常常基于tcp的连接方式
 *  无连接：请求时建连接、请求完释放连接，以尽快将资源释放出来服务其他客户端。Keep-Alive 功能使客户端到服务器端的连接持续有效，当出现对服务器的后继请求时，Keep-Alive 功能避免了建立或者重新建立连接
 *  无状态是指协议对于事务处理没有记忆能力，我们给服务器发送 HTTP 请求之后，服务器根据请求，会给我们发送数据过来，但是，发送完，不会记录任何信息。
 *
 *@Date:Created in 12:24 2018/7/11
 */

import com.horkr.jdk.learn.socket.udp.Receive;
import com.horkr.jdk.learn.socket.udp.Send;

import java.net.DatagramSocket;

public class Demo {
    public static void main(String[] args){
        try {
            DatagramSocket send = new DatagramSocket();
            DatagramSocket rece = new DatagramSocket(10000);
            new Thread(new Send(send)).start();
            new Thread(new Receive(rece)).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
