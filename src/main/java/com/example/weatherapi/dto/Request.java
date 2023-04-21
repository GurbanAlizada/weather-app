package com.example.weatherapi.dto;

public record Request(
        String type,
        String query,
        String language,
        String unit
) {
}
