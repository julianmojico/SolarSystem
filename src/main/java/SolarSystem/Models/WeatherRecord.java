package SolarSystem.Models;

import SolarSystem.Utilities.WeatherDays;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

//@Document(collection = "WeatherRecord"+this.getSolarSystemName())
//@Document(collection = "#{solarSystemManager.getSolarSystem().getSolarSystemName()}")
@Document

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

    @Override
    public boolean equals(Object o){
        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        WeatherRecord weatherRecord = (WeatherRecord) o;
        // field comparison
        return Objects.equals(day, weatherRecord.day)
                && Objects.equals(weatherDay, weatherRecord.weatherDay);

    }

}
