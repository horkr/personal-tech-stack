//package com.horkr.spring.learn.rocketmq.producer;
//
//import org.apache.rocketmq.client.producer.DefaultMQProducer;
//import org.apache.rocketmq.client.producer.TransactionMQProducer;
//
///**
// * @author 卢亮宏
// */
//public class ProducerFactory {
//
//    public static DefaultMQProducer getProducer() {
//        DefaultMQProducer producer = new DefaultMQProducer("gp");
//        producer.setNamesrvAddr("localhost:9876");
//        return producer;
//    }
//
//
//    public static TransactionMQProducer getTransactionProducer() {
//        TransactionMQProducer producer = new TransactionMQProducer("gp");
//        producer.setNamesrvAddr("localhost:9876");
//        return producer;
//    }
//}
