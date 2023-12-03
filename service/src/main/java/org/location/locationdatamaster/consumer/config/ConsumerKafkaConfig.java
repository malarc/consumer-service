package org.location.locationdatamaster.consumer.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Sinks;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class ConsumerKafkaConfig {

    private final ProducerKafkaProperties producerKafkaProperties;

    @Bean
    public KafkaReceiver<String, String> kafkaReceiver(ReceiverOptions<String, String> receiverOptions) {
        return KafkaReceiver.create(receiverOptions);
    }

    @Bean
    public ReceiverOptions<String, String> kafkaReceiverOptions(@Value(value = "apmm.locationref.topic.internal.any.v1") String topic, KafkaProperties kafkaProperties) {
        ProducerKafkaProperties.Producer producerProperties = producerKafkaProperties.getProducer();
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "pkc-6ojv2.us-west4.gcp.confluent.cloud:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "lkc-0djrwp");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put("security.protocol", producerKafkaProperties.getSecurityProtocol());
        props.put("sasl.mechanism",producerKafkaProperties.getSaslMechanism());
        props.put("sasl.jaas.config", producerKafkaProperties.getLoginModule() + " required username=\""
                    + producerKafkaProperties.getUsername() + "\"" + " password=" + "\""
                    + producerKafkaProperties.getPassword() + "\" ;");

        ReceiverOptions<String, String> basicReceiverOptions = ReceiverOptions.create(props);
        return basicReceiverOptions.subscription(Collections.singletonList(topic));
    }



    @Bean
    public ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate(ReceiverOptions<String, String> kafkaReceiverOptions) {
        return new ReactiveKafkaConsumerTemplate<String, String>(kafkaReceiverOptions);
    }

}
