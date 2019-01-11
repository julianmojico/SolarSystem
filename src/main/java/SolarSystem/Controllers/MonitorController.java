package SolarSystem.Controllers;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitorController {

    @RequestMapping(value = "/monitor", method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
    public JSONObject monitor() {
        String output;
        JSONObject obj = new JSONObject();
        obj.put("response","The system is up and running");
        return obj;
    }
}
