package SolarSystem.Implementations;

import SolarSystem.Interfaces.SolarSystemInterface;
import SolarSystem.Models.Planet;
import SolarSystem.Models.SolarSystem;
import SolarSystem.Models.Weather;
import SolarSystem.Services.SolarSystemService;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.PolygonsSet;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.euclidean.twod.hull.ConvexHull2D;
import org.apache.commons.math3.geometry.euclidean.twod.hull.MonotoneChain;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
//A specific implementation of SolarSystem with custom logic and calculations.
public class SolarSystemManager implements SolarSystemService {

    //    @Autowired
    //private static final Logger logger = Logger.getLogger(SolarSystemManager.class.getName());
    private final Logger logger = Logger.getLogger(SolarSystemManager.class.getName());


    private SolarSystemInterface solarSystem;
    private String name;
    //this is the count of days passed since the beginning of this solar system

    private Planet timeReference;
    private boolean timeReferenceSet = false;
    private static final double PIPI = 2 * Math.PI;
    //counters for planets aligned
    public ArrayList<Integer> planetsAligned;
    public ArrayList<Integer> planetsAlignedWithSun;
    public ArrayList<Integer> rainyDays;
    public HashMap<Integer, Double> maxRainyDays;

    @Autowired
    Weather weather;
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

    @Autowired
    public SolarSystemManager(SolarSystem solarSystem) {
        this.solarSystem = solarSystem;
    }

    public SolarSystemManager(String name, double x, double y) {


        this.name = name;
        Vector2D solarCenter = new Vector2D(x, y);
        SolarSystem ss = new SolarSystem(name, solarCenter);
        setSolarSystem(ss);

        //LinkedHashSet classes do not allow duplicates and allows orderered access to element
        solarSystem.setPlanets(new LinkedHashSet());
        planetsAligned = new ArrayList<Integer>();
        planetsAlignedWithSun = new ArrayList<Integer>();
        rainyDays = new ArrayList<Integer>();
        maxRainyDays = new HashMap<Integer, Double>();
        //map inicialization needed in this case
        maxTriangleSize = 0;
        logger.log(Level.INFO, "SolarSystem created and initiated");
    }

    @Override
    public void timePassSequence(int days) {
        //move the planets day by day. Initial planets positions is always aligned so its not computed.

        int ageDays = solarSystem.getAgeDays();
        if (days > 0) {
            if (timeReferenceSet) {

                for (int i = 0; i < days; i++) {

                    //encapsular en un setter
                    solarSystem.setAgeDays(ageDays++);
                    solarSystem.getPlanets().forEach(planet -> {
                        planet.updateAngle();
                    });

                    planetsAligned();
                    planetsAlignedWithSun();
                    sunInsideTriangle();
                    saveResults();
                }
            } else {
                //TODO: ESTA CATCHEADA?
                throw new UnsupportedOperationException("SolarSystem needs to setTimeReference() before timePass() call");
            }
        } else {
            throw new UnsupportedOperationException("Days cannot be negative or null");
        }
    }

    //logic of planet alignment against each other (not with the sun)
    public void planetsAligned() {
        //attempts line alignment using the first planet in the list against the rest of the planets (not sun alignment)

        int ageDays = solarSystem.getAgeDays();
        Iterator<Planet> planetsIterator = solarSystem.getPlanets().iterator();

        if (solarSystem.getPlanets().size() > 2) {

            Planet pivot = planetsIterator.next();
            Vector2D pivotLocation = pivot.getPointLocation();
            Planet second = planetsIterator.next();
            Vector2D secondPlanetLocation = second.getPointLocation();
            Line referenceLine = new Line(pivotLocation, secondPlanetLocation, 0.1);
            boolean planetsAligned = true;

            while (planetsIterator.hasNext() && planetsAligned == true) {
                Planet planet = planetsIterator.next();
                planetsAligned = referenceLine.contains(planet.getPointLocation());
            }

            //save this day if the planets were aligned
            if (planetsAligned) {
                String solarSystemName = solarSystem.getName();
                this.planetsAligned.add(ageDays);
                logger.log(Level.INFO, "Day " + ageDays + ": Planets are aligned in SolarSystem " + solarSystemName);
            }
        } else {

        }
    }

    @Override
    public void planetsAlignedWithSun() {

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

            this.planetsAlignedWithSun.add(ageDays);
            logger.log(Level.INFO, "Day: " + ageDays + " . All the planets are aligned with the sun at angle " + first.normalizedAngle);
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

//            Vector2D[] vertices = {p1,p2,p3};
            PolygonsSet plane = new PolygonsSet(0.0001, p1, p2, p3);


            Collection<Vector2D> vertices = new ArrayList<Vector2D>();
            vertices.add(p1);
            vertices.add(p2);
            vertices.add(p3);

            //ConvexHull2D hull = new ConvexHull2D(vertices,0.0001);

            //esta tira exception
            ConvexHull2D hull = new MonotoneChain().generate(vertices);

            //this check is to avoid a bug where the generate() from hull produces a smaller vertices array than expected
            if (hull.getVertices().length == vertices.size()) {

                int ageDays = solarSystem.getAgeDays();
                if (ageDays == 359) {
                    System.out.println("debug");
                }

                Region<Euclidean2D> region = hull.createRegion();


                Region.Location sunInside = region.checkPoint(sunCenter);


                //check if the size of the triangle is max.
                // double currentTriangleSize = plane.getSize();
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

                if (sunInside == Region.Location.INSIDE) {
                    rainyDays.add(ageDays);
                    logger.log(Level.INFO, "Day " + ageDays + " . Rainy day computed.");
                }

            } else {
                System.out.println("Here is the bug");
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
        Planet betasoide = new Planet("Betasoide", 2000, true, 3);
        Planet vulcano = new Planet("Vulcano", 1000, false, 5);
        addPlanet(ferengi);
        addPlanet(betasoide);
        addPlanet(vulcano);
        setTimeReference(0);
    }

    public void saveResults() {

        HashMap results = new HashMap();

        planetsAlignedWithSun.forEach(
                item -> {}
        );
    }
}
