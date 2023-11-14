package org.location.locationdatamaster.consumer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Parents {
    private static final long serialVersionUID = 1L;
    private String name;
    private String type;
    private String bdaType;
    private List<AlternateCodes> alternateCodes;
}
