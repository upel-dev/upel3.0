package upeldev.com.github.upel3;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Upel3Application {

    public static void main(String[] args) {
        SpringApplication.run(Upel3Application.class, args);
    }

    @Bean
    public CommandLineRunner populateData(DataLoader dataLoader) {
        return args -> dataLoader.populateData();
    }
}
