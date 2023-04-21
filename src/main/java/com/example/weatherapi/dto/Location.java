package com.example.weatherapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Location(

        String  name,
        String country,
        String region,
        String lat,
        String lon,
        @JsonProperty("timezone_id")
        String timeZoneId,
        @JsonProperty("localtime")
        String localTime,
        @JsonProperty("localtime_epoch")
        Integer localTimeEpoch,
        @JsonProperty("utc_offset")
        String utcOffset

) {
}
