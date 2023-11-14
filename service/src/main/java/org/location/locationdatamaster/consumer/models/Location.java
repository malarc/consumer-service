package org.location.locationdatamaster.consumer.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location {
    private static final long serialVersionUID = 1L;

    private List<BusinessDefinedArea> bdas;
    private String name;
    private String locationId;
    private String status;
    private String bdaType;
    private Country country;
    private String geoType;
    private List<Parents> parents;
    private String hsudName;
    private String latitude;
    private String portFlag;
    private String timeZone;
    private String longitude;
    private String validFrom;
    private String restricted;
    private String description;
    private String dialingCode;
    private List<BdaLocations> bdaLocations;
    private String isMaerskCity;
    private String olsonTimezone;
    private List<AlternateNames> alternateNames;
    private String subCityParents;
    private String utcOffsetMinutes;
    private String workaroundReason;
    private String daylightSavingEnd;
    private String daylightSavingTime;
    private String daylightSavingStart;
    private String postalCodeMandatory;
    private String dialingCodeDescription;
    private String stateProvinceMandatory;
    private String daylightSavingShiftMinutes;
    private List<AlternateCodes> alternateCodes;

}
