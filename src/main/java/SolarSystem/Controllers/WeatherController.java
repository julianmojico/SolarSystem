package SolarSystem.Controllers;

import SolarSystem.Exceptions.BadRequestException;
import SolarSystem.Exceptions.ResourceNotFoundException;
import SolarSystem.Exceptions.ServerErrorException;
import SolarSystem.Implementations.SolarSystemManager;
import SolarSystem.Models.WeatherRecord;
import SolarSystem.Repositories.WeatherRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class WeatherController {

    @Autowired
    private SolarSystemManager ssm;
    @Autowired
    private WeatherRepository weatherRepo;

    @RequestMapping(value = "/weather/day/{day}", method = RequestMethod.GET, headers = "Accept=application/json", produces = {"application/json"})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public WeatherRecord getWeather(@PathVariable(value = "day") Integer day) throws Exception {

        if (!(day instanceof Integer)) {
            throw new BadRequestException();
        } else {
            Optional<WeatherRecord> result = weatherRepo.findById(day);
            if (result.isPresent()){
                return result.get();
            } else {
                throw new ResourceNotFoundException();
            }
        }
    }

    @RequestMapping(value = "/weather/{weatherDay}", method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
    public String getWeatherCount(@PathVariable("weatherDay") String weatherDay) throws ServerErrorException {

        //In case of exception, the ExceptionHandler will catch.
        int output = weatherRepo.countByWeatherDay(weatherDay);
        JSONObject obj = new JSONObject();
        obj.put("response:", output);
        return obj.toJSONString();

    }

    @RequestMapping(value="/weather", method = RequestMethod.DELETE, headers = "Accept=application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public String deleteWeather() throws ServerErrorException {

        try {
            weatherRepo.deleteAll();
        } catch (Exception e) {
            throw new ServerErrorException();
        }
        JSONObject obj = new JSONObject();
        obj.put("response:", "All weather records in Astral Solar System were deleted");
        return obj.toJSONString();
    }

    @RequestMapping(value = "/weather/compute", method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
    public String compute(@RequestParam(value = "days", defaultValue = "3650") int days) throws ServerErrorException {

        //This method allows compute and persistance of the weather.

        String output;
        JSONObject obj = new JSONObject();

        try {
            this.ssm.initialize("Astral", 0, 0);
            ssm.setupSampleSystem();
            ssm.timePassSequence(days);

        } catch (Exception e) {
            e.printStackTrace();

            output = "Request could not be processed because of internal server error." + e.getCause();
            obj.put("response", output);
            return obj.toJSONString();
        }

        output = "WeatherDays computed succesfully days " + days + " for SolarSystem " + ssm.getSolarSystem().getName();
        obj.put("response", output);
        return obj.toJSONString();
    }

    @ExceptionHandler({ServerErrorException.class})
    public void handleException() {
    }

}
