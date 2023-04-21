package com.example.weatherapi.repository;

import com.example.weatherapi.model.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WeatherEntityRepository extends JpaRepository<WeatherEntity , String> {

    Optional<WeatherEntity> findFirstByRequestedCityNameOrderByUpdatedTimeDesc(String city);

    List<WeatherEntity> findAllByUpdatedTimeBefore(LocalDateTime updatedTime);

}
