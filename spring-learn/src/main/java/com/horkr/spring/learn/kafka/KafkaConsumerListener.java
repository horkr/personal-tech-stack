package com.horkr.spring.learn.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 卢亮宏
 */
@Component
public class KafkaConsumerListener {
    private static final Logger log = LogManager.getLogger(KafkaConsumerListener.class);
    private static final String LISTENER_ID = "local-listener";
    private static final String TOPICS = "local-topic1";

    /**
     * 对kafka数据批量做持久化到数据库的监听器
     *
     * @param records 消息集合
     */
    @KafkaListener(id = LISTENER_ID, groupId = "local-consumer-group", topics = TOPICS, autoStartup = "false", concurrency = "1")
    public void listener(ConsumerRecords<String, String> records, Consumer<String, String> consumer, Acknowledgment ack) {
        log.info("接收到消息条数：{}", records.count());
        defaultConsume(records, ack);
    }


    /**
     * 默认的消费方式，单线程串行，消费一条提交一次offset
     *
     * @param records 消息集合
     * @param ack     ack
     */
    private void defaultConsume(ConsumerRecords<String, String> records, Acknowledgment ack) {
        for (ConsumerRecord<String, String> record : records) {
            try {
                log.info("消费到消息[{}:{}],分区：{}", record.key(), record.value(), record.partition());
                ack.acknowledge();
            } catch (Exception e) {
                log.error("处理kafka原始消息时异常,消息：{}", record.value(), e);
            }
        }
    }

    /**
     * offset 提交方式，按照业务需要自行选择
     *
     * @param records  消息集合
     * @param consumer consumer
     */
    private void offsetCommit(ConsumerRecords<String, String> records, Consumer<String, String> consumer) {
        Set<TopicPartition> partitions = records.partitions();
        log.info("消息来自于分区：{}", partitions.stream().map(TopicPartition::partition).map(String::valueOf).collect(Collectors.joining(",")));
        for (TopicPartition partition : partitions) {
            log.info("开始处理分区：{}数据", partition.partition());
            // 指定分区的记录
            List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
            for (ConsumerRecord<String, String> record : partitionRecords) {
                log.info("消费到消息[{}:{}],分区：{}", record.key(), record.value(), record.partition());
                // 1. 逐条同步提交
                consumer.commitSync(buildOffset(TOPICS, partition.partition(), record.offset()));
            }
            //2. 按照分区提交
            long offset = partitionRecords.get(partitionRecords.size() - 1).offset();
            consumer.commitSync(buildOffset(TOPICS,partition.partition(),offset));
        }
        //3.按消费批次提交
        consumer.commitAsync();
    }


    private Map<TopicPartition, OffsetAndMetadata> buildOffset(String topic, int partition, long offset) {
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(offset);
        Map<TopicPartition, OffsetAndMetadata> map = new HashMap<>();
        map.put(topicPartition, offsetAndMetadata);
        return map;
    }
}
