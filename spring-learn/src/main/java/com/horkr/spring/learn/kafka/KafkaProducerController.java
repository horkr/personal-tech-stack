//package com.horkr.spring.learn.kafka;
//
//import com.horkr.util.dto.PairData;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.apache.kafka.clients.producer.RecordMetadata;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//
///**
// * @author 卢亮宏
// */
//@RestController
//public class KafkaProducerController {
//    private static final Logger log = LogManager.getLogger(KafkaProducerController.class);
//    @Resource
//    private KafkaTemplate<String, Object> kafkaTemplate;
//
//
//    /**
//     * 发送键值消息
//     */
//    @GetMapping("/sendKafkaMessagee")
//    public void sendMessageKeyValue(@RequestBody PairData<String, Object> pairData) {
//        log.info("发送key-value消息");
//        try {
//            SendResult sendResult = kafkaTemplate.send("local-topic1", pairData.getKey(), pairData.getValue()).get();
//            RecordMetadata recordMetadata = sendResult.getRecordMetadata();
//            log.info("topic: {},  partition：{}， offset: {}",
//                    recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 发送键值消息
//     */
//    @GetMapping("/sendBatchMessage")
//    public void sendBatchMessage(int messageSize) {
//        int partitionSize = 3;
//        for (int i = 0; i < messageSize; i++) {
//            int partition = i % partitionSize;
//            kafkaTemplate.send("local-topic1", partition, String.valueOf(i), RandomStringUtils.random(3,"abcdefghijklmnopquvwxyzrst"));
//        }
//        log.info("send success!");
//    }
//}
