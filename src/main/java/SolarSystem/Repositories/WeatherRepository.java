package SolarSystem.Repositories;

import SolarSystem.Models.Weather;
import SolarSystem.Utilities.WeatherDays;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
public interface WeatherRepository extends  MongoRepository<Weather, Integer>{


}
