package com.example.weatherapi.dto;

public record WeatherResponse(
        Request request,
        Current current,
        Location location

        ){
}
