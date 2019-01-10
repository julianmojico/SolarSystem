package SolarSystem.Models;

import SolarSystem.Utilities.WeatherDays;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "WeatherRecord")

public class WeatherRecord {

    @Id
    private int day;
    @Indexed
    private WeatherDays weatherDay;

    public WeatherRecord(int day, WeatherDays weatherDay) {
        this.day = day;
        this.weatherDay = weatherDay;
    }

    public int getDay() {
        return day;
    }

    public WeatherDays getWeatherDay() {
        return weatherDay;
    }

//    @Override
//    @JsonSerialize
//    public String toString() {
//        return String.format("The day %s will be %s",day,weatherDay);
//    }
}
