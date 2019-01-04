package SolarSystem.Implementations;

import SolarSystem.Models.Planet;
import SolarSystem.Models.SolarSystem;
import SolarSystem.Services.SolarSystemService;
import SolarSystem.Utilities.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
//A specific implementation of SolarSystem with custom logic and calculations.
public class SolarSystemManager implements SolarSystemService {

    @Autowired
    private Tuple tuple;
    private static final Logger logger = Logger.getLogger(SolarSystemManager.class.getName());
    @Autowired
    private SolarSystem solarSystem;
    private String name;
    //this is the count of days passed since the beginning of this solar system
    private int daysPassed;
    private Planet timeReference;
    private boolean timeReferenceSet = false;
    private static final double PIPI = 2*Math.PI;

    public Planet getTimeReference() {
        return timeReference;
    }

    //Set the planet which rules the calendar for the solarsystem.
    public void setTimeReference(int planetNumber) {

        if (planetNumber > this.solarSystem.getPlanets().size()) {
            throw new IndexOutOfBoundsException("Planet number is bigger than the size of Planets");
        } else {

            Planet currentPlanet = null;
            for (int i = 0; i != planetNumber; i++) {
                currentPlanet = solarSystem.getPlanets().iterator().next();
            }
                this.timeReference = currentPlanet;
                this.timeReferenceSet = true;
        }

    }


    public void setLogger(Logger logger){
        logger = logger;
    }

    //TODO: ESTA INSTANCIA LOGGER ES UNICA SI CREO DOS SOLARSYSTEMIMPL?

    public SolarSystemManager(){

    }

    public SolarSystemManager(String name, double x, double y) {

        this.name = name;
        tuple = new Tuple(x,y);
        setSolarSystem(new SolarSystem(name, tuple));

        //LinkedHashSet classes do not allow duplicates and allows orderered access to element
        solarSystem.setPlanets(new LinkedHashSet() );
        logger.log(Level.INFO, "SolarSystem created");
    }

    @Override
    public void timePass(int days) {

       if (days>0){
        if (timeReferenceSet){
            solarSystem.ageDays +=days;
            solarSystem.getPlanets().forEach( planet -> {
                this.turnsToSun(days, planet);

            });
        } else {
            //TODO: ESTA CATCHEADA?
            throw new UnsupportedOperationException("SolarSystem needs to setTimeReference() before timePass() call");
        }
    }
       else {
        throw new UnsupportedOperationException("Days cannot be negative or null");
       }
    }

    private void turnsToSun(int days, Planet planet){

        //clockwise turns increment positively and counterclock increment negatively.


        int turnsToSun=0;
        double newAngleRads = 0;

        if (planet.clockwise) {
             newAngleRads = Math.toRadians(planet.angleByDaySpeed*days);
             while (newAngleRads > PIPI  ){
                newAngleRads-=PIPI;
                turnsToSun++;
        } } else {

             newAngleRads = Math.toRadians((-1)*planet.angleByDaySpeed*days);
            while (newAngleRads < -PIPI  ){
                newAngleRads+=PIPI;
                turnsToSun++;
            }
        }
        planet.turnsToSun = turnsToSun;
        planet.actualAngle = newAngleRads;
        planet.age+=days;
        logger.log(Level.INFO,"The planet "+planet.name+" did "+ turnsToSun +" turns to the sun in days="+days);
    };

    @Override
    public boolean planetsAlignedWithSun() {

       // StraightLine2D line;
        return false;
    }

    @Override
    public void getCurrentStatus() {

    }

    @Override
    public void addPlanet(Planet p) {

        //All positions are positioned in 0 degrees initially
        p.actualAngle = 0;
        solarSystem.getPlanets().add(p);
        //this implementation places planets in the 90• angle for the initial position
        logger.log(Level.INFO, "Planet "+p.name+" was added to " + name);
    }

    public boolean planetsAligned() {
        return false;
    }

    @Override
    public boolean sunInsideTriangle() {
        return false;
    }

    public SolarSystem getSolarSystem() {
        return solarSystem;
    }

    public void setSolarSystem(SolarSystem solarSystem) {
        this.solarSystem = solarSystem;
    }
}
