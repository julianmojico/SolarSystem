package SolarSystem.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitorController {

    @RequestMapping("/monitor")
    public String monitor() {
        return "The system is up and running";
    }

}
