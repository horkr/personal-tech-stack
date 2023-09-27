//package com.horkr.spring.learn.rocketmq.producer;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
//import org.apache.rocketmq.client.producer.LocalTransactionState;
//import org.apache.rocketmq.client.producer.TransactionMQProducer;
//import org.apache.rocketmq.common.message.Message;
//import java.nio.charset.StandardCharsets;
//
///**
// * @author 卢亮宏
// */
//public class TransactionMsg {
//    private static final Logger log = LogManager.getLogger(TransactionMsg.class);
//
//    private static void transactionDemo() throws Exception {
//        TransactionMQProducer producer = ProducerFactory.getTransactionProducer();
//
//        producer.setTransactionCheckListener(msg -> {
//            log.info("执行事务校验，msg:{}",msg );
//            // 查询本地事务是否成功，按需发送事务状态
//            return LocalTransactionState.UNKNOW;
//        });
//        producer.start();
//        // 本地事务执行器
//        LocalTransactionExecuter localTransactionExecuter = (msg, arg) -> {
//            try {
//                // 执行本地事务
//                log.info("执行本地事务，msg:{},arg:{}", new String(msg.getBody()), arg);
//                return LocalTransactionState.COMMIT_MESSAGE;
//            }catch (Exception e){
//                return LocalTransactionState.ROLLBACK_MESSAGE;
//            }
//        };
//  
//        log.info("发消息");
//        producer.sendMessageInTransaction(new Message("local-tp2", "first msg---".getBytes(StandardCharsets.UTF_8)), localTransactionExecuter, null);
//
//    }
//
//    public static void main(String[] args) throws Exception {
//        transactionDemo();
//    }
//}
