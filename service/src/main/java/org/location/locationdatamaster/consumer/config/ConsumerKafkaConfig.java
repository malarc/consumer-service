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
        // props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        //props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "2097164");
        //props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        //props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG,"3000");
        props.put("security.protocol", producerKafkaProperties.getSecurityProtocol());
        props.put("sasl.mechanism",producerKafkaProperties.getSaslMechanism());
        props.put("sasl.jaas.config", producerKafkaProperties.getLoginModule() + " required username=\""
                    + producerKafkaProperties.getUsername() + "\"" + " password=" + "\""
                    + producerKafkaProperties.getPassword() + "\" ;");
/*            props.put("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username=\"YGZWSL4IE7B7EUKG\" \n" +
                    "password=\"yXBPPcK4ZIHRII0ybeGpFaXsLDJn8HBFHLnvgkJC8SApSQ46mhyR/NnOmbzVu0WF\";");*/

        ReceiverOptions<String, String> basicReceiverOptions = ReceiverOptions.create(props);
        return basicReceiverOptions.subscription(Collections.singletonList(topic));
    }


    /*private void addProducerProperties(Map<String, Object> properties) {
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        ProducerKafkaProperties.Producer producerProperties = producerKafkaProperties.getProducer();
        properties.put(ProducerConfig.LINGER_MS_CONFIG, producerProperties.getLinger());
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, producerProperties.getBatchSize());
        properties.put(ProducerConfig.SEND_BUFFER_CONFIG, producerProperties.getBatchSize());
        properties.put(ProducerConfig.ACKS_CONFIG, producerProperties.getAcksConfig());
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, producerProperties.getTimeout());
        properties.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, producerProperties.getConnectionsMaxIdle());
        properties.put(ProducerConfig.METADATA_MAX_AGE_CONFIG, producerProperties.getMetadataMaxAge());
        properties.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, producerProperties.getMaxRequestSize());
        properties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, producerProperties.getDeliveryTimeout());

    }*/

    @Bean
    public ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate(ReceiverOptions<String, String> kafkaReceiverOptions) {
        return new ReactiveKafkaConsumerTemplate<String, String>(kafkaReceiverOptions);
    }

}
