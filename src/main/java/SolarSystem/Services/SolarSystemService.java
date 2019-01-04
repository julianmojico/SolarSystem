package SolarSystem.Services;

import SolarSystem.Models.SolarSystem;
import org.springframework.beans.factory.annotation.Autowired;

//App designed to  handle multiple different solar systems instances
public interface SolarSystemService {

    //void setSolarSystem(double centerX, double centerY); //crear sistema solar y alinear planetas
    void timePass(int days);
    boolean planetsAlignedWithSun();
    boolean planetsAligned();
    void getCurrentStatus();


}
