package org.location.locationdatamaster.consumer.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is model class for response returned when POST is issued for submission request
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KafkaSubmissionResponse {
    @JsonProperty("messageKey")
    private String messageKey;

}
