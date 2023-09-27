package com.horkr.spring.learn.rocketmq.producer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author 卢亮宏
 */
public class Consumer {
    private static final Logger log = LogManager.getLogger(Consumer.class);

    public static void consume() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("local_consumer1");
        consumer.setNamesrvAddr("localhost:9876");
        // *表示不过滤
        consumer.subscribe("local-tp1", "*");
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                log.info("消费：{}", new String(msg.getBody()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
    }

    public static void main(String[] args) throws Exception {
        consume();
    }
}
