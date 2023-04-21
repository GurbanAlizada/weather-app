package com.example.weatherapi.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class Constants {

    public static String WEATHER_STACK_API_BASE_URL;
    public static final String WEATHER_STACK_API_ACCESS_KEY_PARAM = "?access_key=";
    public static String API_KEY;
    public static final String WEATHER_STACK_API_QUERY_PARAM = "&query=";



    public static Integer API_CALL_LIMIT;
    public static String WEATHER_CACHE_NAME;


    @Value("${weather-stack.base-url}")
    public void setWeatherStackApiBaseUrl(String weatherStackApiBaseUrl) {
        WEATHER_STACK_API_BASE_URL = weatherStackApiBaseUrl;
    }


    @Value("${weather-stack.api-key}")
    public void setApiKey(String apiKey) {
        API_KEY = apiKey;
    }


}
