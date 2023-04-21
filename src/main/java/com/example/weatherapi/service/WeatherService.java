package com.example.weatherapi.service;

import com.example.weatherapi.constants.Constants;
import com.example.weatherapi.dto.WeatherDto;
import com.example.weatherapi.dto.WeatherResponse;
import com.example.weatherapi.exception.CityNotFoundException;
import com.example.weatherapi.model.WeatherEntity;
import com.example.weatherapi.repository.WeatherEntityRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = {"weaters"})
public class WeatherService {

    private final Logger logger =  LoggerFactory.getLogger(WeatherService.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final RestTemplate restTemplate;
    private final WeatherEntityRepository weatherEntityRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public WeatherService(RestTemplate restTemplate, WeatherEntityRepository weatherEntityRepository) {
        this.restTemplate = restTemplate;
        this.weatherEntityRepository = weatherEntityRepository;
    }


    @Cacheable(key = "#city")
    public WeatherDto getWeatherByCityName(String city){

        logger.info("Requested city : " + city);
        Optional<WeatherEntity> weatherEntity = weatherEntityRepository.findFirstByRequestedCityNameOrderByUpdatedTimeDesc(city);

        return weatherEntity.map(weather -> {
            if (weather.getUpdatedTime().isBefore(LocalDateTime.now().minusMinutes(30))){
                return WeatherDto.convert(getWeatherFromWeatherStackApi(city));
            }

            logger.info("Get weather info from DB , city : "  + city);
            return WeatherDto.convert(weather);

        }).orElseGet(()-> WeatherDto.convert(getWeatherFromWeatherStackApi(city)));

    }

    @PostConstruct
    @CacheEvict(allEntries = true)
    @Scheduled(fixedDelay = 120000 , initialDelay = 10000)
    public void clearCache(){
        logger.info("cleared cache : " + LocalDateTime.now());
    }


    private WeatherEntity getWeatherFromWeatherStackApi(String city){

        logger.info("get weather info from API , city: " + city);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity( getWeatherStackUrl(city) , String.class);
        WeatherResponse weatherResponse ;
        WeatherEntity weatherEntity ;

        try {
            weatherResponse = objectMapper.readValue(responseEntity.getBody() , WeatherResponse.class);
            weatherEntity = saveWeatherEntity(city, weatherResponse);
        } catch (JsonProcessingException e) {
            throw new CityNotFoundException("City Not Found");
        }


        return weatherEntity;
    }

    private String getWeatherStackUrl(String city){
        return Constants.WEATHER_STACK_API_BASE_URL + Constants.WEATHER_STACK_API_ACCESS_KEY_PARAM + Constants.API_KEY + Constants.WEATHER_STACK_API_QUERY_PARAM + city;
    }



    private WeatherEntity saveWeatherEntity(String city , WeatherResponse weatherResponse){

        WeatherEntity weatherEntity = new WeatherEntity(
                city ,
                weatherResponse.location().name() ,
                weatherResponse.location().country() ,
                weatherResponse.current().temperature() ,
                LocalDateTime.now() ,
                LocalDateTime.parse(weatherResponse.location().localTime() , formatter)
        );

        final var fromDb = weatherEntityRepository.save(weatherEntity);
        return fromDb;
    }




}
