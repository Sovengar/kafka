package com.devtiro.kafkatutorial.kafka.config;

import com.devtiro.kafkatutorial.kafka.events.Event;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {
    private final KafkaConfigProps kafkaConfigProps;

    // 1. Consume string data from Kafka

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigProps.getBootstrapAddress());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "loggroup");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    // 2. Consume event objects from Kafka

//    public ConsumerFactory<String, Event<?>> objectConsumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigProps.getBootstrapAddress());
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConfigProps.getCustomerGroupId());
//        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//
//        final JsonDeserializer<Event<?>> jsonDeserializer = new JsonDeserializer<>();
//        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), jsonDeserializer);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, Event<?>> objectKafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, Event<?>> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(objectConsumerFactory());
//        return factory;
//    }
}
