package com.horkr.spring.learn.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author 卢亮宏
 */
@Component
public class KafkaConsumerListener {
    private static final Logger log = LogManager.getLogger(KafkaConsumerListener.class);
    private static final String LISTENER_ID = "local-listener";
    /**
     * 对kafka数据批量做持久化到数据库的监听器
     *
     * @param list 消息实体
     */
    @KafkaListener(id = LISTENER_ID, groupId = "local-consumer-group", topics = "local-topic1", autoStartup = "true",concurrency = "3")
    public void listener(ConsumerRecords<String, String> list, Acknowledgment ack){
        for (ConsumerRecord<String, String> record : list) {
            try {
                log.info("消费到消息[{}:{}],分区：{}",record.key(),record.value(),record.partition());
                ack.acknowledge();
            } catch (Exception e) {
                log.error("处理kafka原始消息时异常,消息：{}", record.value(), e);
            }
        }
    }
}
