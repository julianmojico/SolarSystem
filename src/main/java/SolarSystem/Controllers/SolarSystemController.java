package SolarSystem.Controllers;

import SolarSystem.Exceptions.ServerErrorException;
import SolarSystem.Implementations.SolarSystemManager;
import SolarSystem.Models.WeatherRecord;
import SolarSystem.Repositories.WeatherRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/astral")
public class SolarSystemController {

    @Autowired
    private SolarSystemManager ssm;
    @Autowired
    private WeatherRepository weatherRepo;

    @RequestMapping(value = "/weather/day/{day}", method = RequestMethod.GET, headers = "Accept=application/json", produces = {"application/json"})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public WeatherRecord getWeather(@PathVariable(value = "day") int day) throws ServerErrorException {

        return weatherRepo.findById(day).get();

    }

    @RequestMapping(value = "/weather/{weatherDay}", method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
    public String getWeatherCount(@PathVariable("weatherDay") String weatherDay) throws ServerErrorException {
        int output = weatherRepo.countByWeatherDay(weatherDay);
        JSONObject obj = new JSONObject();
        obj.put("response:", output);
        return obj.toJSONString();

    }

    @RequestMapping(value = "/weather/compute", method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
    public String compute(@RequestParam(value = "days", defaultValue = "3650") int days) throws ServerErrorException {

        //This method allows compute and persistance of the weather.

        String output;
        JSONObject obj = new JSONObject();
        this.ssm.initialize("Astral", 0, 0);

        try {

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
