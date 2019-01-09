package SolarSystem.Interfaces;

import SolarSystem.Models.Planet;
import SolarSystem.Models.SolarSystem;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.Collection;

public interface SolarSystemInterface {


   Collection<Planet> getPlanets();
   void setPlanets(Collection<Planet> planets);
   Vector2D getSolarCenter();
   void setSolarCenter(Vector2D solarCenter);
   int getAgeDays();
   void setAgeDays(int days);
   String getName();
}
