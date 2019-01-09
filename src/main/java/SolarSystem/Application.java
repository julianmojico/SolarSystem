package SolarSystem;

import SolarSystem.Implementations.SolarSystemManager;
import SolarSystem.Models.Planet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"boot.registration","SolarSystem"})
@EnableAutoConfiguration
@EntityScan("SolarSystem.Utilities")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}