package SolarSystem.Controllers;

import SolarSystem.Implementations.SolarSystemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system")
public class BeansController {

    @Autowired
    ApplicationContext ctx;


    @RequestMapping(value = "/beans")
    @ResponseBody
    public Object getBeans() {

        int beans = ctx.getBeansOfType(SolarSystemManager.class).size();
        String output = ("There are " + beans + " beans instances for SolarSystemManager");
        System.out.println(output);
        return output;

    }
}
