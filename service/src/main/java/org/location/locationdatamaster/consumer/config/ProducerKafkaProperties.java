package org.location.locationdatamaster.consumer.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "consumer-kafka-config")
@Data
public class ProducerKafkaProperties {

    private String bootstrapServers ;
    private String clientId ;
    private String username;
    private String password;
    private String loginModule = "org.apache.kafka.common.security.plain.PlainLoginModule";
    private String saslMechanism = "PLAIN";
    private String securityProtocol = "SASL_SSL";
    private String truststoreLocation;
    private String truststorePassword;
    private String topic;

    private Producer producer = new Producer();

    @Getter
    @Setter
    @ToString
    public static class Producer {
        private String acksConfig = "all"; //1
        private int linger = 1;
        private int timeout = 30000;
        private int batchSize = 16384;
        private int sendBuffer = 131072;
        private int connectionsMaxIdle = 180000;
        private int metadataMaxAge = 180000;
        private int maxRequestSize = 1000000;
        private int requestTimeout = 30000;
        private int deliveryTimeout = 120000;
    }
}
