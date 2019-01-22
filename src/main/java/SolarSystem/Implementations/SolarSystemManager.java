package SolarSystem.Implementations;

import SolarSystem.Interfaces.SolarSystemInterface;
import SolarSystem.Models.Planet;
import SolarSystem.Models.SolarSystem;
import SolarSystem.Models.WeatherRecord;
import SolarSystem.Services.SolarSystemService;
import SolarSystem.Utilities.GeometryService;
import SolarSystem.Utilities.WeatherDays;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

@Service
@Scope(value = "prototype")
//A specific implementation of SolarSystem with custom logic and calculations.
public class SolarSystemManager implements SolarSystemService {

    private static final double PIPI = 2 * Math.PI;
    public HashMap<Integer, WeatherRecord> weatherRecords;
    //day counter for each use case of alignment
    private Logger logger = LoggerFactory.getLogger(SolarSystemManager.class);
    public HashMap<Integer, Double> maxRainyDays;

    private SolarSystemInterface solarSystem;
    private String name;
    private Planet timeReference;
    private boolean timeReferenceSet = false;
    private double maxTriangleSize;

    public Planet getTimeReference() {
        return timeReference;
    }

    //Set the planet which rules the calendar for the solarsystem.
    private void setTimeReference(int planetNumber) {

        if (planetNumber > this.solarSystem.getPlanets().size()) {
            throw new IndexOutOfBoundsException("Planet number is bigger than the size of Planets");
        } else {

            Planet currentPlanet = null;
            for (int i = 0; i != planetNumber + 1; i++) {
                currentPlanet = solarSystem.getPlanets().iterator().next();
            }
            this.timeReference = currentPlanet;
            this.timeReferenceSet = true;
        }

    }

    public void SolarSystemManager() {

    }

    public void initialize(String name, double x, double y) {

        this.name = name;
        Vector2D solarCenter = new Vector2D(x, y);
        SolarSystem ss = new SolarSystem(name, solarCenter);
        setSolarSystem(ss);

        //LinkedHashSet classes do not allow duplicates and allows orderered access to element
        solarSystem.setPlanets(new LinkedHashSet());
        maxRainyDays = new HashMap<Integer, Double>();
        //map inicialization
        maxTriangleSize = 0;
        logger.info("SolarSystem created and initiated");
    }

    @Override
    public void timePassSequence(int days) {

        //move the planets day by day. Initial planets positions is always aligned so its not computed.
        int ageDays = solarSystem.getAgeDays();
        weatherRecords = new HashMap<Integer, WeatherRecord>(days);

        if (days > 0) {
            if (timeReferenceSet) {
                for (int i = 0; i <= days; i++) {

                    //in case there is intersection between the weather conditions, the first case is prioritized
                    if (!planetsAlignedWithSun()) {
                        if (!planetsAligned()) {
                            sunInsideTriangle();
                        }
                    }
                    solarSystem.getPlanets().forEach(planet -> {
                        planet.updateAngle();
                    });
                    ageDays++;
                    solarSystem.setAgeDays(ageDays);
                }
                populateMaxRainyDay();
            } else {
                throw new UnsupportedOperationException("SolarSystem needs to setTimeReference() before timePass() call");
            }
        } else {
            throw new UnsupportedOperationException("Days cannot be negative or null");
        }
    }

    private void populateMaxRainyDay() {

        this.maxRainyDays.forEach(
                (day, weather) -> {
                    WeatherRecord wr = new WeatherRecord(day, WeatherDays.MAX_RAIN);
                    weatherRecords.put(day, wr);
                });
    }


    //logic of planet alignment against each other (not with the sun)
    public boolean planetsAligned() {

        int ageDays = solarSystem.getAgeDays();
        boolean planetsAligned = GeometryService.planetsAligned(solarSystem);

        if (planetsAligned) {
            //save weatherRecord if the planets were aligned
            String solarSystemName = solarSystem.getName();
            WeatherRecord wr = new WeatherRecord(ageDays, WeatherDays.MILD);
            weatherRecords.put(ageDays, wr);
            logger.info("Day " + ageDays + ": Planets are aligned in SolarSystem ");
        }
        return planetsAligned;
    }

    @Override
    public boolean planetsAlignedWithSun() {

        int ageDays = solarSystem.getAgeDays();
        boolean alignedWithTheSun = true;
        Iterator<Planet> it = solarSystem.getPlanets().iterator();
        Planet first = it.next();
        while (it.hasNext() && alignedWithTheSun) {
            Planet next = it.next();
            //planets aligned with sun if the angle is the same or complementary
            alignedWithTheSun = (Math.abs(first.normalizedAngle) == Math.abs(next.normalizedAngle)) || (Math.abs(first.normalizedAngle - next.normalizedAngle) == 180);
        }
        if (alignedWithTheSun) {

            WeatherRecord wr = new WeatherRecord(ageDays, WeatherDays.DRY);
            weatherRecords.put(ageDays, wr);
            logger.info("Day: " + ageDays + " . All the planets are aligned with the sun at angle " + first.normalizedAngle);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addPlanet(Planet p) {

        //All positions are positioned in 0 degrees initially
        p.angleRads = 0;
        solarSystem.getPlanets().add(p);
        //this implementation places planets in the 90• angle for the initial position
        logger.info("Planet " + p.name + " was added to " + name);
    }

    @Override
    public void sunInsideTriangle() {

        int ageDays = solarSystem.getAgeDays();
        WeatherRecord wr = null;
        WeatherDays weatherDays;

        //use case valid for 3 planets only
        if (solarSystem.getPlanets().size() == 3) {

            /*Geometry service call*/
            double triangleSize = GeometryService.getPlanetsSunTriangleSize(solarSystem);

            /* if triangle size is 0 then its not a rainy day*/
            if (triangleSize > 0) {
                weatherDays = WeatherDays.RAINY;
            } else {
                weatherDays = WeatherDays.NONE;
            }

            /*check if the size of the triangle is max and store it*/
            updateMaxRainyDays(triangleSize);

            /*save weather record*/
            wr = new WeatherRecord(ageDays, weatherDays);
            logger.info("Day " + ageDays + ":" + weatherDays + " day computed");
            weatherRecords.put(ageDays, wr);

        } else {
            logger.warn("Triangulation is possible when solar system contains 3 planets only");
        }
    }

    private void updateMaxRainyDays(double triangleSize) {

        if (triangleSize >= maxTriangleSize) {
            maxTriangleSize = triangleSize;

            //if max found, remove smaller records.
            Iterator it = maxRainyDays.keySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, Double> element = (Map.Entry) it.next();
                if (element.getValue() < maxTriangleSize) {
                    it.remove();
                }

                maxRainyDays.put(solarSystem.getAgeDays(), maxTriangleSize);
            }
        }

    }


    public SolarSystemInterface getSolarSystem() {
        return solarSystem;
    }

    private void setSolarSystem(SolarSystem solarSystem) {
        this.solarSystem = solarSystem;
    }

    public void setupSampleSystem() {

        Planet ferengi = new Planet("Ferengi", 500, true, 1);
        Planet betasoide = new Planet("Betasoide", 2000, true, 2);
        Planet vulcano = new Planet("Vulcano", 1000, false, 5);
        addPlanet(ferengi);
        addPlanet(betasoide);
        addPlanet(vulcano);
        setTimeReference(0);
    }

}
