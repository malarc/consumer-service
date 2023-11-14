package org.location.locationdatamaster.consumer.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.location.locationdatamaster.consumer.service.KafkaConsumerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@Validated
@AllArgsConstructor
public class KafkaController {

    private final KafkaConsumerService consumerService;


    @GetMapping("/consume")
    public Flux<String> consumeMessages() {
        return consumerService.consumeMessages();
    }
}
