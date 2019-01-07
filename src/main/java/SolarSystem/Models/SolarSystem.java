package SolarSystem.Models;

import SolarSystem.Utilities.Tuple;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Scope(value = "prototype")

public class SolarSystem {

    //Generic class so any implementation of collection can be chosen.
    private Collection<Planet> planets;
    private Tuple solarCenter;
    public int ageDays;
    public String name;


    public  SolarSystem(String name, Tuple solarCenter){

        this.name = name;
        solarCenter = solarCenter;
    }

    public Collection<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(Collection<Planet> planets) {
        this.planets = planets;
    }

    public Tuple getSolarCenter() {
        return solarCenter;
    }



}
