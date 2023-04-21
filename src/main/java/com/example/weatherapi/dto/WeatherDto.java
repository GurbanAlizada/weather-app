package com.example.weatherapi.dto;

import com.example.weatherapi.model.WeatherEntity;

import java.time.LocalDateTime;

public record WeatherDto(
        String city ,
        String country,
        Integer temperature,
        LocalDateTime updatedTime

) {
    public static WeatherDto convert(WeatherEntity from){
        return new WeatherDto(from.getCityName() , from.getCountry(), from.getTemperature() , from.getUpdatedTime());
    }

}
