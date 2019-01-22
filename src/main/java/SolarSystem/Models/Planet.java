package SolarSystem.Models;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.geom.Point2D;
import java.util.logging.Logger;

@Component
@Scope(value = "prototype")
public class Planet {

    private static final double PIPI = 2 * Math.PI;
    private static final Logger logger = Logger.getLogger(Planet.class.getName());
    public String name;
    public Point2D actualPosition;
    //radians angle
    public double angleRads;
    //sexagecimal angle normalized
    public int normalizedAngle;
    //Sexagecimal angle acumulative
    private double angle;
    public int turnsToSun;
    private double distanceToSun;
    private int angleByDaySpeed;
    private boolean clockwise;
    private int age;
    private double x;
    private double y;


    public Planet(String name, double distanceToSun, boolean clockwise, int angleByDay) {
        age = 0;
        this.name = name;
        this.angleByDaySpeed = angleByDay;
        this.distanceToSun = distanceToSun;
        this.clockwise = clockwise;
        //asumming 0,0 is always the relative position of the sun in the solar system
        angle = 0;
    }

    public void updateAngle() {
        //clockwise turns increment positively and counterclock increment negatively.
        if (clockwise) {
            angle += angleByDaySpeed;
        } else {
            angle -= angleByDaySpeed;
        }

        normalizedAngle = (int) (angle % 360);
        angleRads = Math.toRadians(normalizedAngle);
        updateLocation();
        age++;
    }

    private void updateLocation() {
        //updates planet position in x,y according to new angle

        //x=radius*cos(angle)
        x = distanceToSun * Math.cos(normalizedAngle);
        //y=radius*cos(angle)
        y = distanceToSun * Math.sin(normalizedAngle);

    }

    public Vector2D getPointLocation() {
        return new Vector2D(x, y);
    }

}
