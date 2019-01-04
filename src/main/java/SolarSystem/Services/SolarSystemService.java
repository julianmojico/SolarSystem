package SolarSystem.Services;

import SolarSystem.Models.Planet;
import SolarSystem.Models.SolarSystem;
import org.springframework.beans.factory.annotation.Autowired;

//App designed to  handle multiple different solar systems instances
public interface SolarSystemService {

    void timePass(int days);
    boolean planetsAlignedWithSun();
    boolean planetsAligned();
    boolean sunInsideTriangle();
    void getCurrentStatus();
    void addPlanet(Planet p);


}
