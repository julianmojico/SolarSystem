package SolarSystem.Repositories;

import SolarSystem.Models.WeatherRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends MongoRepository<WeatherRecord, Integer> {

    @Query(value = "{'weatherDay':'DRY'}", count = true)
    int dryCount() throws Exception;

    int countByWeatherDay(String weatherDay);

}
