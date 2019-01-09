package SolarSystem.Controllers;

import SolarSystem.Exceptions.ServerErrorException;
import SolarSystem.Implementations.SolarSystemManager;
import SolarSystem.Models.SolarSystem;
import SolarSystem.Repositories.WeatherRepository;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.json.*;
import java.util.Collections;

@RestController
public class SolarSystemController {

    @Autowired
    private SolarSystemManager solarSystemManager;
    @Autowired
    private WeatherRepository weatherRepo;

    private void setSolarSystemManager(SolarSystemManager scm){
        this.solarSystemManager=scm;
    }



    @RequestMapping(value = "/weather", method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
    public String getWeather(@RequestParam(value="day", defaultValue = "1" ) int day) {

//        weatherRepo.
        return "test";
    }



    @RequestMapping(value="/computeWeather",method= RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
    public String weather(@RequestParam(value="days", defaultValue = "1" ) int days)  throws ServerErrorException {

        //This method allows compute and persistance of the weather.

        String output;
        JSONObject obj = new JSONObject();

       try {

           SolarSystemManager scm = new SolarSystemManager("Astral",0,0);
           this.setSolarSystemManager(scm);
           solarSystemManager.setupSampleSystem();
           solarSystemManager.timePassSequence(1001);
           solarSystemManager.saveResults();

       } catch (Exception e) {
           e.printStackTrace();

           output="Request could not be processed because of internal server error."+e.getCause();
           obj.put("response",output);
           return obj.toJSONString();
    }


     output = "WeatherDays computed succesfully days "+days+" for SolarSystem "+ solarSystemManager.getSolarSystem().getName();


        obj.put("response",output);
       return obj.toJSONString();
}

    @ExceptionHandler({ ServerErrorException.class })
    public void handleException() {
        //
    }

}
