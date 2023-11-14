package org.location.locationdatamaster.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.location.locationdatamaster.consumer.models.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.publisher.Sinks;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;


@RequiredArgsConstructor
@EnableKafka
@Slf4j
@Service
public class KafkaConsumerService {


    private final ReactiveKafkaConsumerTemplate<String, String> kafkaConsumerTemplate;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    @EventListener(ApplicationStartedEvent.class)
    public Flux<String> consumeMessages() {
        log.info("Kafka message received  : :");
        return kafkaConsumerTemplate.receiveAutoAck()
                .map(record -> {
                    String key = record.key();
                    String value = record.value();
                    log.info("Received message:  {}" + value);
                    Location loc = getJsonAsObject(value, Location.class);
                    log.info("Location message:  {}" + loc);
                    return value;
                });
    }

    private static <T> T getJsonAsObject(String json, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (JsonProcessingException ex) {
            log.error("error while Deserializing input json {}", json, ex);
            return null;
        }
    }


}

