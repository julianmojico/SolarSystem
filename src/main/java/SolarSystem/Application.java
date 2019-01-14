package SolarSystem;

import SolarSystem.Models.SolarSystem;
import com.mongodb.MongoSocketReadException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {"boot.registration", "SolarSystem", "SolarSystem.Repositories", "SolarSystem.Test"})
@EnableAutoConfiguration
@EnableMongoRepositories()
public class Application {

    public static void main(String[] args) {
            SpringApplication.run(Application.class, args);

    }

}