package SolarSystem.Implementations;

import SolarSystem.Interfaces.SolarSystemInterface;
import SolarSystem.Models.Planet;
import SolarSystem.Models.SolarSystem;
import SolarSystem.Models.WeatherRecord;
import SolarSystem.Repositories.WeatherRepository;
import SolarSystem.Services.SolarSystemService;
import SolarSystem.Utilities.WeatherDays;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.PolygonsSet;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.euclidean.twod.hull.ConvexHull2D;
import org.apache.commons.math3.geometry.euclidean.twod.hull.MonotoneChain;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
@Scope(value = "prototype")
//A specific implementation of SolarSystem with custom logic and calculations.
public class SolarSystemManager implements SolarSystemService {

    private static final double PIPI = 2 * Math.PI;
    private final Logger logger = Logger.getLogger(SolarSystemManager.class.getName());
    //counters for planets aligned
    public ArrayList<Integer> planetsAligned;
    public ArrayList<Integer> planetsAlignedWithSun;
    //this is the count of days passed since the beginning of this solar system
    public ArrayList<Integer> rainyDays;
    public HashMap<Integer, Double> maxRainyDays;
    @Autowired
    private WeatherRepository weatherRepository;
    private SolarSystemInterface solarSystem;
    private String name;
    private Planet timeReference;
    private boolean timeReferenceSet = false;
    private double maxTriangleSize;

    public Planet getTimeReference() {
        return timeReference;
    }

    //Set the planet which rules the calendar for the solarsystem.
    public void setTimeReference(int planetNumber) {

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


    public void setLogger(Logger logger) {
        logger = logger;
    }

    //TODO: ESTA INSTANCIA LOGGER ES UNICA SI CREO DOS SOLARSYSTEMIMPL?


    public void SolarSystemManager() {

    }

    public void initialize(String name, double x, double y) {

        this.name = name;
        Vector2D solarCenter = new Vector2D(x, y);
        SolarSystem ss = new SolarSystem(name, solarCenter);
        setSolarSystem(ss);

        //drop collection in case weather was previously computed with different values
        weatherRepository.deleteAll();

        //LinkedHashSet classes do not allow duplicates and allows orderered access to element
        solarSystem.setPlanets(new LinkedHashSet());
        planetsAligned = new ArrayList<Integer>();
        planetsAlignedWithSun = new ArrayList<Integer>();
        rainyDays = new ArrayList<Integer>();
        maxRainyDays = new HashMap<Integer, Double>();
        //map inicialization
        maxTriangleSize = 0;
        logger.log(Level.INFO, "SolarSystem created and initiated");
    }

    @Override
    public void timePassSequence(int days) {

        //move the planets day by day. Initial planets positions is always aligned so its not computed.
        int ageDays = solarSystem.getAgeDays();
        if (days > 0) {
            if (timeReferenceSet) {
                for (int i = 0; i <= days; i++) {

                    //in case there is intersection between the weather conditions, the first case is prioritized
                    if (!planetsAlignedWithSun()) {
                        if (!planetsAligned()) {
                            sunInsideTriangle();
                            populateMaxRainyDay();
                        }
                    }
                    solarSystem.getPlanets().forEach(planet -> {
                        planet.updateAngle();
                    });
                    ageDays++;
                    solarSystem.setAgeDays(ageDays);
                }
            } else {
                //TODO: ESTA CATCHEADA?
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
                    if (weatherRepository.existsById(wr.getDay())) {
                        weatherRepository.deleteById(wr.getDay());
                        weatherRepository.save(wr);
                    } else {
                        weatherRepository.save(wr);
                    }
                });
    }


    //logic of planet alignment against each other (not with the sun)
    public boolean planetsAligned() {
        //attempts line alignment using the first planet in the list against the rest of the planets (not sun alignment)

        int ageDays = solarSystem.getAgeDays();
        Iterator<Planet> planetsIterator = solarSystem.getPlanets().iterator();
        boolean planetsAligned = true;

        if (solarSystem.getPlanets().size() > 2) {

            Planet pivot = planetsIterator.next();
            Vector2D pivotLocation = pivot.getPointLocation();
            Planet second = planetsIterator.next();
            Vector2D secondPlanetLocation = second.getPointLocation();
            Line referenceLine = new Line(pivotLocation, secondPlanetLocation, 50);

            while (planetsIterator.hasNext() && planetsAligned == true) {
                Planet planet = planetsIterator.next();
                planetsAligned = referenceLine.contains(planet.getPointLocation());
            }

            //save this day if the planets were aligned
            if (planetsAligned) {
                String solarSystemName = solarSystem.getName();
//                this.planetsAligned.add(ageDays);
                WeatherRecord wr = new WeatherRecord(ageDays, WeatherDays.MILD);
                if (weatherRepository.existsById(wr.getDay())) {
                    weatherRepository.deleteById(wr.getDay());
                    weatherRepository.save(wr);
                } else {
                    weatherRepository.save(wr);
                }

                logger.log(Level.INFO, "Day " + ageDays + ": Planets are aligned in SolarSystem " + solarSystemName);
            }
        }
        return planetsAligned;
    }

    @Override
    public boolean planetsAlignedWithSun() {

        int ageDays = solarSystem.getAgeDays();
        boolean alignedWithTheSun = true;
        Iterator<Planet> it = solarSystem.getPlanets().iterator();
        Planet first = it.next();
        while (it.hasNext() && alignedWithTheSun == true) {
            Planet next = it.next();
            //planets aligned with sun if the angle is the same or complementary
            alignedWithTheSun = (Math.abs(first.normalizedAngle) == Math.abs(next.normalizedAngle)) || (Math.abs(first.normalizedAngle - next.normalizedAngle) == 180);
        }
        if (alignedWithTheSun) {

//            this.planetsAlignedWithSun.add(ageDays);
            WeatherRecord wr = new WeatherRecord(ageDays, WeatherDays.DRY);
            if (weatherRepository.existsById(wr.getDay())) {
                weatherRepository.deleteById(wr.getDay());
                weatherRepository.save(wr);
            } else {
                weatherRepository.save(wr);
            }
            logger.log(Level.INFO, "Day: " + ageDays + " . All the planets are aligned with the sun at angle " + first.normalizedAngle);
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
        logger.log(Level.INFO, "Planet " + p.name + " was added to " + name);
    }

    @Override
    public void sunInsideTriangle() {

        if (solarSystem.getPlanets().size() == 3) {

            Vector2D sunCenter = new Vector2D(0, 0);
            Iterator<Planet> it = solarSystem.getPlanets().iterator();

            Vector2D p1 = it.next().getPointLocation();
            Vector2D p2 = it.next().getPointLocation();
            Vector2D p3 = it.next().getPointLocation();

            PolygonsSet plane = new PolygonsSet(0.0001, p1, p2, p3);

            Collection<Vector2D> vertices = new ArrayList<Vector2D>();
            vertices.add(p1);
            vertices.add(p2);
            vertices.add(p3);

            ConvexHull2D hull = new MonotoneChain().generate(vertices);

            //this check is to avoid a bug where the generate() from hull produces a smaller vertices array than expected
            if (hull.getVertices().length == vertices.size()) {

                int ageDays = solarSystem.getAgeDays();
                Region<Euclidean2D> region = hull.createRegion();
                Region.Location sunInside = region.checkPoint(sunCenter);

                //check if the size of the triangle is max.
                double currentTriangleSize = region.getSize();

                if (currentTriangleSize >= maxTriangleSize) {
                    maxTriangleSize = currentTriangleSize;
                    for (Integer item : maxRainyDays.keySet()) {
                        if (item < maxTriangleSize) {
                            maxRainyDays.keySet().remove(item);
                        }
                    }
                    maxRainyDays.put(ageDays, maxTriangleSize);
                }
                WeatherRecord wr = null;
                if (sunInside == Region.Location.INSIDE) {

                    wr = new WeatherRecord(ageDays, WeatherDays.RAINY);
                    logger.log(Level.INFO, "Day " + ageDays + " . Sun is inside planet triangulation");
                } else {
                    wr = new WeatherRecord(ageDays, WeatherDays.NONE);
                    logger.log(Level.INFO, "Day " + ageDays + " . None day computed.");
                }

                if (weatherRepository.existsById(wr.getDay())) {
                    weatherRepository.deleteById(wr.getDay());
                    weatherRepository.save(wr);
                } else {
                    weatherRepository.save(wr);
                }
            }
        } else {
            logger.log(Level.WARNING, "Triangulation is possible when solar system contains 3 planets only");
        }
    }

    public SolarSystemInterface getSolarSystem() {
        return solarSystem;
    }

    public void setSolarSystem(SolarSystem solarSystem) {
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

    @Autowired
    public void setWeatherRepository(WeatherRepository weatherRepository) {

        this.weatherRepository = weatherRepository;
    }
}
