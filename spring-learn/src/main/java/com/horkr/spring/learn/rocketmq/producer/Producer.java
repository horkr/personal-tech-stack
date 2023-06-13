package com.horkr.spring.learn.rocketmq.producer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * 1.批量发送
 * @see DefaultMQProducer#send(Collection)
 *
 * 2.
 * @author 卢亮宏
 */
public class Producer {
    private static final Logger log = LogManager.getLogger(Producer.class);
    private static void demo1() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("gp");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();
        Message message = new Message("local-tp1","first msg".getBytes(StandardCharsets.UTF_8));
        sendAsync(producer,message);
        producer.shutdown();
    }

    /**
     * 同步发送
     */
    private static SendResult sendSync(DefaultMQProducer producer,Message message ) throws Exception{
        return producer.send(message);
    }


    /**
     * 异步发送
     */
    private static void sendAsync(DefaultMQProducer producer,Message message ) throws Exception{
        producer.send(message,new SendCallback() {

            public void onSuccess(SendResult sendResult) {
                log.info(sendResult);
            }

            public void onException(Throwable e) {
               log.error("发送失败",e);
            }
        });
    }

    public static void main(String[] args)  throws Exception{
        demo1();
    }
}
