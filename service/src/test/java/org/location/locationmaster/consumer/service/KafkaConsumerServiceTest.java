package org.location.locationmaster.consumer.service;


import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.location.locationdatamaster.consumer.config.ConsumerKafkaConfig;
import org.location.locationdatamaster.consumer.domains.LocationMaster;
import org.location.locationdatamaster.consumer.models.Location;
import org.location.locationdatamaster.consumer.repositories.LocationRepository;
import org.location.locationdatamaster.consumer.service.KafkaConsumerService;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { KafkaConsumerService.class })
//@ContextConfiguration(classes = ConsumerKafkaConfig.class)
@DirtiesContext
public class KafkaConsumerServiceTest {


    @MockBean
    private ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate;

    @MockBean
    private  LocationRepository locationRepository;

    @MockBean
    private KafkaConsumerService kafkaConsumerService ;

    @Test
    @SneakyThrows
    void testKafkaService() {

        Class<?>[] params= new Class<?>[]{};
        Method method1= KafkaConsumerService.class.getDeclaredMethod("consumeMessages", params);
        method1.setAccessible(true);
        method1.invoke(kafkaConsumerService);
        Class<?>[] param= new Class<?>[]{Location.class};
        Method method = KafkaConsumerService.class.getDeclaredMethod("mapLocationMasterData", param);
        Location loc = Location.builder().name("city").status("Active").locationId("123456").geoType("east").build();
        method.setAccessible(true);
        method.invoke(kafkaConsumerService, loc);
        Class<?>[] paramSave= new Class<?>[]{LocationMaster.class,LocationRepository.class};
        Method methodSave = KafkaConsumerService.class.getDeclaredMethod("saveEntity", paramSave);
        LocationMaster locationMaster = LocationMaster.builder().locationName("city").status("Active").locationId("123456").locationType("east").build();
        methodSave.setAccessible(true);
        methodSave.invoke(kafkaConsumerService, locationMaster, locationRepository);
    }

}
