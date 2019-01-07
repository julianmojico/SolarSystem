package SolarSystem.Services;

import SolarSystem.Models.Planet;
import SolarSystem.Models.SolarSystem;
import org.springframework.beans.factory.annotation.Autowired;

//App designed to  handle multiple different solar systems instances
public interface SolarSystemService {

    void timePassSequence(int days);
    void planetsAlignedWithSun();
    void planetsAligned() throws Exception;
    /**Computes days when the Sun is inside the triangle of the 3 planets.
     * This method is valid when there are only 3 planets.**/
    void sunInsideTriangle();
    void addPlanet(Planet p);




}
