package SolarSystem.Services;

import SolarSystem.Models.Planet;
import org.springframework.stereotype.Service;

@Service
//App designed to  handle multiple different solar systems instances
public interface SolarSystemService {

    void timePassSequence(int days);

    boolean planetsAlignedWithSun();

    boolean planetsAligned() throws Exception;

    /**
     * Computes days when the Sun is inside the triangle of the 3 planets.
     * This method is valid when there are only 3 planets.
     **/
    void sunInsideTriangle();

    void addPlanet(Planet p);


}
