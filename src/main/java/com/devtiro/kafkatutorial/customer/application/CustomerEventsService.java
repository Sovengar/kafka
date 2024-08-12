package com.devtiro.kafkatutorial.customer.application;

import com.devtiro.kafkatutorial.kafka.config.KafkaConfigProps;
import com.devtiro.kafkatutorial.customer.domain.Customer;
import com.devtiro.kafkatutorial.customer.events.CustomerCreatedEvent;
import com.devtiro.kafkatutorial.kafka.events.Event;
import com.devtiro.kafkatutorial.kafka.events.EventType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerEventsService {
    private final KafkaTemplate<String, String> stringProducer;
    private final KafkaTemplate<String, Event<?>> objectProducer;
    private final KafkaConfigProps kafkaConfigProps;

    // PRODUCER
    public void publish(Customer customer) {
        CustomerCreatedEvent created = new CustomerCreatedEvent();
        created.setData(customer);
        created.setId(UUID.randomUUID().toString());
        created.setType(EventType.CREATED);
        created.setDate(new Date());

        CompletableFuture<SendResult<String, Event<?>>> future = this.objectProducer.send(kafkaConfigProps.getCustomerTopicName(), created);
        future.whenComplete((result, ex) -> {
            long offset = result.getRecordMetadata().offset();
            feedbackMessageSent(created.toString(), offset, ex);
        });
    }

    public void publish(String message) {
        CompletableFuture<SendResult<String, String>> future = this.stringProducer.send(kafkaConfigProps.getLogTopicName(), message);
        future.whenComplete((result, ex) -> {
            long offset = result.getRecordMetadata().offset();
            feedbackMessageSent(message, offset, ex);
        });
    }

    private void feedbackMessageSent(String message, Long offset, Throwable ex) {
        if (ex == null) {
            log.info("Sent message=[{}] with offset=[{}]", message, offset);
        } else {
            log.info("Unable to send message=[{}] due to: {}", message, ex.getMessage());
        }
    }

    // CONSUMER
    @KafkaListener(topics = "${custom.kafka.customerTopicName}", groupId = "${custom.kafka.customerGroupId}", containerFactory = "objectKafkaListenerContainerFactory")
    public void consume(Event<?> event, ConsumerRecord<?, ?> record) {
        int partition = record.partition();
        long offset = record.offset();

        if (event.getClass().isAssignableFrom(CustomerCreatedEvent.class)) {
            CustomerCreatedEvent customerCreatedEvent = (CustomerCreatedEvent) event;
            log.info("Received Customer created event ... with Id={}, data={} from partition: {} at offset: {}", customerCreatedEvent.getId(), customerCreatedEvent.getData().toString(), partition, offset);
        }
    }

    @KafkaListener(topics = "${custom.kafka.logTopicName}", groupId = "${custom.kafka.logGroupId}", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload String message, ConsumerRecord<?, ?> record) {
        int partition = record.partition();
        long offset = record.offset();
        log.info("Received Message: {} from partition: {} at offset: {}", message, partition, offset);
    }
}

//@KafkaListener(
//        topicPartitions = @TopicPartition(topic = "topicName",
//                partitionOffsets = {
//                        @PartitionOffset(partition = "0", initialOffset = "0"),
//                        @PartitionOffset(partition = "3", initialOffset = "0")}),
//        containerFactory = "partitionsKafkaListenerContainerFactory")

//Since the initialOffset has been set to 0 in this listener, all the previously consumed messages from partitions 0 and 3 will be re-consumed every time this listener is initialized.
//If we don’t need to set the offset, we can use the partitions property of @TopicPartition annotation to set only the partitions without the offset:

//@KafkaListener(topicPartitions = @TopicPartition(topic = "topicName", partitions = { "0", "1" }))
