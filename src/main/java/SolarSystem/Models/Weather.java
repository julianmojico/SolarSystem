package SolarSystem.Models;

import SolarSystem.Utilities.WeatherDays;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class Weather {

    private HashMap<Integer, WeatherDays> totalDays;
}
