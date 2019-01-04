package SolarSystem;

import SolarSystem.Implementations.SolarSystemImpl;
import SolarSystem.Models.Planet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication(scanBasePackages = {"boot.registration"})
@EnableAutoConfiguration
@EntityScan("SolarSystem.Utilities")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private static void run(String... args) {

        SolarSystemImpl mySolarSystem = new SolarSystemImpl(0,0);
        Planet vulcano = new Planet(500, (short) 1, true);
        System.out.println();
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