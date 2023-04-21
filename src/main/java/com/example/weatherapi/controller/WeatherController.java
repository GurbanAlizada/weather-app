package com.example.weatherapi.controller;


import com.example.weatherapi.validation.CityNameConstraint;
import com.example.weatherapi.dto.WeatherDto;
import com.example.weatherapi.service.WeatherService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/weather")
@Validated
@Tag(name= "APP" , description = "Weather App")
public class WeatherController {

    private final WeatherService weatherService;


    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }


    //  " "
    @GetMapping("/{city}")
    @RateLimiter(name = "basic")
    @Operation(description = "we can check the city temperature using this api")
    public ResponseEntity<WeatherDto> getWeatherByCityName(@PathVariable @NotBlank @CityNameConstraint  String city){
        return ResponseEntity.ok(weatherService.getWeatherByCityName(city));
    }


}
