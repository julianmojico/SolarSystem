package SolarSystem.Controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class MonitorController {

    @RequestMapping("/monitor")
    public String monitor() {
        return "The system is up and running";
    }

}
