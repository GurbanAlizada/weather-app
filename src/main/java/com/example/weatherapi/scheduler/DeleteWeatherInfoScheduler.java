package com.example.weatherapi.scheduler;


import com.example.weatherapi.model.WeatherEntity;
import com.example.weatherapi.repository.WeatherEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DeleteWeatherInfoScheduler {

    private final WeatherEntityRepository weatherEntityRepository;
    private final Logger logger = LoggerFactory.getLogger(DeleteWeatherInfoScheduler.class);


    public DeleteWeatherInfoScheduler(WeatherEntityRepository weatherEntityRepository) {
        this.weatherEntityRepository = weatherEntityRepository;
    }


    @Scheduled(fixedDelayString = "1440000000" , initialDelay = 60000)
    public void deleteWeatherInfo(){
        logger.info("delete oldest informations  : " + LocalDateTime.now());
        LocalDateTime localDateTime = LocalDateTime.now().minusSeconds(30);
        List<WeatherEntity> weatherEntities = weatherEntityRepository.findAllByUpdatedTimeBefore(localDateTime);
        weatherEntities.stream().forEach(
                n->weatherEntityRepository.delete(n)
        );
    }


}
