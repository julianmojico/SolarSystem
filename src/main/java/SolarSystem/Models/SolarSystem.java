package SolarSystem.Models;

import SolarSystem.Utilities.Tuple;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Scope(value = "prototype")

public class SolarSystem {


    private ArrayList<Planet> planets;
    private Tuple solarCenter;

    public  SolarSystem(Tuple solarCenter){
        this.solarCenter = solarCenter;
    }

    public ArrayList<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(ArrayList<Planet> planets) {
        this.planets = planets;
    }

    public Tuple getSolarCenter() {
        return solarCenter;
    }


    public void setSolarCenter(Tuple solarCenter) {
        this.solarCenter = solarCenter;

    }

}
