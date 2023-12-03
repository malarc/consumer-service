package org.location.locationdatamaster.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.location.locationdatamaster.consumer.domains.LocationMaster;
import org.location.locationdatamaster.consumer.models.Location;
import org.location.locationdatamaster.consumer.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.EnableKafka;

import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



import java.nio.charset.StandardCharsets;


@RequiredArgsConstructor
@EnableKafka
@Slf4j
@Service
public class KafkaConsumerService  implements CommandLineRunner {

    @Value("classpath:schema.sql")
    private Resource initSql;

    private final R2dbcEntityTemplate entityTemplate;


    private final ReactiveKafkaConsumerTemplate<String, String> kafkaConsumerTemplate;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    private final LocationRepository locationRepository;

    @EventListener(ApplicationStartedEvent.class)
    public Flux<Object> consumeMessages() {
        log.info("Kafka message received  : :");
        return kafkaConsumerTemplate.receiveAutoAck()
                .map(record -> {
                    var locationMaster = mapLocationMasterData(getJsonAsObject(record.value(), Location.class));
                    log.info("Received message:  {}" + record.value());
                    return   saveEntity(locationMaster)
                            .map(savedEntity -> ResponseEntity.status(HttpStatus.CREATED).body("Entity saved successfully"))
                            .doOnError(DataAccessException.class, ex -> log.error(ex.toString()))
                            .doOnError(Exception.class, ex -> log.error(ex.toString()));
                });
    }

    private LocationMaster mapLocationMasterData(Location location){
        return   LocationMaster.builder().locationId(location.getLocationId())
                .locationName(location.getName())
                .status(location.getStatus())
                .locationType(location.getGeoType())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }

    private Mono<String> saveEntity(LocationMaster locationMaster) {
        if(StringUtils.hasText(locationMaster.getLocationName()) && StringUtils.hasText(locationMaster.getLocationType())
            && (StringUtils.hasText(locationMaster.getStatus()) && locationMaster.getStatus().equals("Active"))) {
            locationRepository.save(locationMaster)
                    .onErrorResume(DuplicateKeyException.class, ex -> {
                        System.err.println("Duplicate key violation error: " + ex.getMessage());
                        return Mono.empty();
                    })
                    .subscribe(savedEntity -> {
                      log.info("Entity saved: {}" ,savedEntity);
                    });
        }
        return Mono.empty();

    }

    private static <T> T getJsonAsObject(String json, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (JsonProcessingException ex) {
            log.error("error while Deserializing input json {}", json, ex);
            return null;
        }
    }

    @Override
    public void run(String... args) throws Exception {
        String query = StreamUtils.copyToString(initSql.getInputStream(), StandardCharsets.UTF_8);
        this.entityTemplate
                .getDatabaseClient()
                .sql(query)
                .then()
                .subscribe();

    }
}

