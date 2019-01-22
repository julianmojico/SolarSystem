package SolarSystem.Utilities;

import SolarSystem.Interfaces.SolarSystemInterface;
import SolarSystem.Models.Planet;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.euclidean.twod.hull.ConvexHull2D;
import org.apache.commons.math3.geometry.euclidean.twod.hull.MonotoneChain;
import org.apache.commons.math3.geometry.partitioning.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class GeometryService {

    /* Returns triangle size if the sun is inside planet's triangle, otherwise 0*/
    public static double getPlanetsSunTriangleSize(SolarSystemInterface solarSystem) {

        double triangleSize = 0;
        Iterator<Planet> it = solarSystem.getPlanets().iterator();

        Vector2D p1 = it.next().getPointLocation();
        Vector2D p2 = it.next().getPointLocation();
        Vector2D p3 = it.next().getPointLocation();

        Collection<Vector2D> vertices = new ArrayList<Vector2D>();
        vertices.add(p1);
        vertices.add(p2);
        vertices.add(p3);

        ConvexHull2D hull = new MonotoneChain().generate(vertices);

        Vector2D sunCenter = solarSystem.getSolarCenter();
        //this check is to avoid a bug where the generate() in Hull class produces a smaller vertices array than expected
        if (hull.getVertices().length == 3) {

            Region<Euclidean2D> region = hull.createRegion();
            Region.Location sunInside = region.checkPoint(sunCenter);

            //check if the size of the triangle is max and store size for max size comparing.

            if (sunInside == Region.Location.INSIDE) {
                triangleSize = region.getSize();
            }
        }
        return triangleSize;
    }

    //attempts line alignment using the first planet in the list against the rest of the planets (not sun alignment)
    public static boolean planetsAligned(SolarSystemInterface solarSystem) {

        Iterator<Planet> planetsIterator = solarSystem.getPlanets().iterator();
        boolean planetsAligned = true;

        if (solarSystem.getPlanets().size() > 2) {

            /*Geometry objects initialization*/
            Planet pivot = planetsIterator.next();
            Vector2D pivotLocation = pivot.getPointLocation();
            Planet second = planetsIterator.next();
            Vector2D secondPlanetLocation = second.getPointLocation();
            Line referenceLine = new Line(pivotLocation, secondPlanetLocation, 50);

            /*Planets iteration and alignment attempt*/
            while (planetsIterator.hasNext() && planetsAligned) {
                Planet planet = planetsIterator.next();
                planetsAligned = referenceLine.contains(planet.getPointLocation());
            }


        }
        return planetsAligned;
    }
}
