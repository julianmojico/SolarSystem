package SolarSystem.Models;

import SolarSystem.Interfaces.SolarSystemInterface;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
//@Scope(value = "prototype")
public class SolarSystem  implements SolarSystemInterface {

    //Generic class so any implementation of collection can be chosen.
    private Collection<Planet> planets;
    private Vector2D solarCenter;
    public int ageDays;
    public String name;


    public  SolarSystem(){};
    public SolarSystem(String name, Vector2D solarCenter)

    {

        this.name = name;
        this.solarCenter = solarCenter;
    }

    public Collection<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(Collection<Planet> planets) {
        this.planets = planets;
    }

    public Vector2D getSolarCenter() {
        return solarCenter;
    }

    @Override
    public void setSolarCenter(Vector2D solarCenter) {
        this.solarCenter = solarCenter;
    }

    @Override
    public int getAgeDays() {
        return this.ageDays;
    }

    @Override
    public void setAgeDays(int days) {
        this.ageDays=days;
    }

    @Override
    public String getName() {
        return this.name;
    }


}
