package com.devtiro.kafkatutorial.customer.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Test {
    //@Payload String message
    //@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition
    @KafkaListener(topics = "log", groupId = "loggroup", containerFactory = "kafkaListenerContainerFactory")
    public String consume(String message) {
        System.out.println("Received Message: " + message + " from partition: " /*+ partition*/);
        return message;
    }
}
