package SolarSystem.Controllers;

import SolarSystem.Implementations.SolarSystemManager;
import SolarSystem.Models.StringResponse;
import SolarSystem.Models.WeatherRecord;
import SolarSystem.Repositories.WeatherRepository;
import SolarSystem.Utilities.WeatherDays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class WeatherController {

    @Autowired
    private SolarSystemManager ssm;
    @Autowired
    private WeatherRepository weatherRepo;

    @RequestMapping(value = "/weather/day/{day}", method = RequestMethod.GET, headers = "Accept=application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getWeather(@PathVariable(value = "day") Integer day) {

        ResponseEntity<?> response;

        Optional<WeatherRecord> result = weatherRepo.findById(day);
        if (result.isPresent()) {
            response = new ResponseEntity<Optional>(result, HttpStatus.OK);

        } else {
            response = new ResponseEntity<StringResponse>(new StringResponse("Day not found or not computed"), HttpStatus.NOT_FOUND);

        }
        return response;
    }

    @RequestMapping(value = "/weather/{weatherDay}", method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
    public ResponseEntity<StringResponse> getWeatherCount(@PathVariable("weatherDay") String weatherDay) {

        //In case of exception, the ExceptionHandler will catch.

        if (WeatherDays.valueOf(weatherDay) != null) {
            int output = weatherRepo.countByWeatherDay(weatherDay);
            StringResponse stringResponse = new StringResponse(Integer.toString(output));
            return new ResponseEntity<StringResponse>(stringResponse, HttpStatus.OK);
        } else {
            StringResponse stringResponse = new StringResponse("Valid weather types are: " + WeatherDays.values().toString());
            return new ResponseEntity<StringResponse>(stringResponse, HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/weather", method = RequestMethod.DELETE, headers = "Accept=application/json", produces = "application/json")
    public @ResponseBody
    ResponseEntity<StringResponse> deleteWeather() {


        weatherRepo.deleteAll();
        String message = "All weather records in Solar System were deleted";
        StringResponse stringResponse = new StringResponse(message);
        return new ResponseEntity<StringResponse>(stringResponse, HttpStatus.OK);

    }

    @RequestMapping(value = "/weather/compute", method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
    public ResponseEntity<StringResponse> compute(@RequestParam(value = "days", defaultValue = "3650") int days) {

        //Controller to perform weather calculations.

        weatherRepo.deleteAll();

        this.ssm.initialize("Astral", 0, 0);
        ssm.setupSampleSystem();
        ssm.timePassSequence(days);

        /*Once computed, store each record in the db*/
        for (WeatherRecord record : ssm.weatherRecords.values()) {
            weatherRepo.save(record);
        }
        String message = "WeatherDays computed succesfully for " + days + " days";
        StringResponse stringResponse = new StringResponse(message);
        return new ResponseEntity<StringResponse>(stringResponse, HttpStatus.OK);
    }
}
