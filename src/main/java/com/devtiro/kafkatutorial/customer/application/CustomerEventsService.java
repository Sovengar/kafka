package com.devtiro.kafkatutorial.customer.application;

import com.devtiro.kafkatutorial.kafka.config.KafkaConfigProps;
import com.devtiro.kafkatutorial.customer.domain.Customer;
import com.devtiro.kafkatutorial.customer.events.CustomerCreatedEvent;
import com.devtiro.kafkatutorial.kafka.events.Event;
import com.devtiro.kafkatutorial.kafka.events.EventType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerEventsService {
    private final KafkaTemplate<String, String> stringProducer;
    //private final KafkaTemplate<String, Event<?>> objectProducer;
    private final KafkaConfigProps kafkaConfigProps;

    // PRODUCER
//    public void publish(Customer customer) {
//        CustomerCreatedEvent created = new CustomerCreatedEvent();
//        created.setData(customer);
//        created.setId(UUID.randomUUID().toString());
//        created.setType(EventType.CREATED);
//        created.setDate(new Date());
//
//        CompletableFuture<SendResult<String, Event<?>>> future = this.objectProducer.send(kafkaConfigProps.getCustomerTopicName(), created);
//        future.whenComplete((result, ex) -> {
//            if (ex == null) {
//                System.out.println("Sent message=[" + created.toString() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
//            } else {
//                System.out.println("Unable to send message=[" + created.toString() + "] due to : " + ex.getMessage());
//            }
//        });
//    }

    public void publish(String message) {
        CompletableFuture<SendResult<String, String>> future = this.stringProducer.send(kafkaConfigProps.getCustomerTopicName(), message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }

    // CONSUMER
//    @KafkaListener(topics = "${custom.kafka.customerTopicName}", groupId = "${custom.kafka.customerGroupId}", containerFactory = "objectKafkaListenerContainerFactory")
//    public void consume(Event<?> event) {
//        if (event.getClass().isAssignableFrom(CustomerCreatedEvent.class)) {
//            CustomerCreatedEvent customerCreatedEvent = (CustomerCreatedEvent) event;
//            log.info("Received Customer created event ... with Id={}, data={}", customerCreatedEvent.getId(), customerCreatedEvent.getData().toString());
//        }
//    }


}

//@KafkaListener(
//        topicPartitions = @TopicPartition(topic = "topicName",
//                partitionOffsets = {
//                        @PartitionOffset(partition = "0", initialOffset = "0"),
//                        @PartitionOffset(partition = "3", initialOffset = "0")}),
//        containerFactory = "partitionsKafkaListenerContainerFactory")
//public void listenToPartition(
//        @Payload String message,
//        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
//    System.out.println("Received Message: " + message + " from partition: " + partition);
//}

//Since the initialOffset has been set to 0 in this listener, all the previously consumed messages from partitions 0 and 3 will be re-consumed every time this listener is initialized.
//If we don’t need to set the offset, we can use the partitions property of @TopicPartition annotation to set only the partitions without the offset:
//@KafkaListener(topicPartitions = @TopicPartition(topic = "topicName", partitions = { "0", "1" }))
