package com.devtiro.kafkatutorial.kafka.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {
    private final KafkaConfigProps kafkaConfigProps;

    @Bean
    public NewTopic logTopic() {
        return TopicBuilder.name(kafkaConfigProps.getLogTopicName())
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic customerTopic() {
        return TopicBuilder.name(kafkaConfigProps.getCustomerTopicName())
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public KafkaAdmin kafkaAdmin() { //This bean is only needed if you are not using spring boot or the topic is not externally-manually created
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigProps.getBootstrapAddress());
        return new KafkaAdmin(configs);
    }
}
