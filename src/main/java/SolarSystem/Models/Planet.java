package SolarSystem.Models;

import javafx.scene.shape.Circle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.geom.Point2D;

@Component
@Scope(value = "prototype")
public class Planet {

    public String name;
    public Circle orbit;
    public Point2D actualPosition;
    //In radians
    public double actualAngle;
    public int turnsToSun;
    public double distanceToSun;
    public int angleByDaySpeed;
    public boolean clockwise;
    public int age;


    public Planet(String name, double distanceToSun, boolean clockwise, int angleByDay){
        age=0;
        this.name = name;
        this.angleByDaySpeed = angleByDay;
        this.distanceToSun = distanceToSun;
        this.clockwise = clockwise;
        //asumming 0,0 is always the relative position of the sun in the solar system
        orbit = new Circle(0,0,distanceToSun);
    }

}
