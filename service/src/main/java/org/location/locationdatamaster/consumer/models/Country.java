package org.location.locationdatamaster.consumer.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {
    private static final long serialVersionUID = 1L;

    private String name;
    private List<AlternateCodes> alternateCodes;

}
