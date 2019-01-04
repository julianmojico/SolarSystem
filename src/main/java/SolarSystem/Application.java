package SolarSystem;

import SolarSystem.Implementations.SolarSystemManager;
import SolarSystem.Models.Planet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"boot.registration"})
@EnableAutoConfiguration
@EntityScan("SolarSystem.Utilities")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private static void run(String... args) {

        SolarSystemManager mySolarSystem = new SolarSystemManager("Astral", 0,0);
        Planet ferengi = new Planet("Ferengi", 500, true, 1);
        Planet betasoide = new Planet("Betasoide", 2000,  true, 3);
        Planet vulcano = new Planet("Vulcano", 1000,  false, 5);
        mySolarSystem.addPlanet(ferengi);
        mySolarSystem.addPlanet(betasoide);
        mySolarSystem.addPlanet(vulcano);
        mySolarSystem.setTimeReference(0);
        mySolarSystem.timePass(1000);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//        return args -> {
//
//            System.out.println("Let's inspect the beans provided by Spring Boot:");
//
//            String[] beanNames = ctx.getBeanDefinitionNames();
//            Arrays.sort(beanNames);
//            for (String beanName : beanNames) {
//                System.out.println(beanName);
//            }
//
//        };
//    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return Application::run;

    }
}