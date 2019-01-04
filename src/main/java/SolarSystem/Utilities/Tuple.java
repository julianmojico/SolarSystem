package SolarSystem.Utilities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Configuration
@Component
//@ComponentScan(basePackageClasses = int.class)
public class Tuple {

    private double x;
    private double y;
    private Tuple tuple;

    public Tuple(double x, double y) {
        this.x = x;
        this.y = y;
    }


    @Bean
    public void createTuple(double x, double y) {
        x = x;
        y = y;
    }
}