package SolarSystem.Models;

import javafx.scene.shape.Circle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.geom.Point2D;

@Component
@Scope(value = "prototype")
public class Planet {

    private Circle orbit;
    private Point2D actualPosition;
    private double distanceToSun;
    private short angleByDaySpeed;
    private boolean clockwise;


    public Planet(double distanceToSun,short angleByDaySpeed, boolean clockwise){
        distanceToSun = distanceToSun;
        angleByDaySpeed = angleByDaySpeed;
        clockwise = clockwise;
        //asumming 0,0 is always the relative position of the sun in the solar system
        orbit = new Circle(0,0,distanceToSun);
    }
}
