package com.example.Banking.App.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
    @Bean
    public NewTopic accountTopic() {
        return TopicBuilder.name("account-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic transactionTopic() {
        return TopicBuilder.name("transaction-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }
    @Bean
    public NewTopic depositTopic() {
        return TopicBuilder.name("deposit-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic loanTopic() {
        return TopicBuilder.name("loan-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
