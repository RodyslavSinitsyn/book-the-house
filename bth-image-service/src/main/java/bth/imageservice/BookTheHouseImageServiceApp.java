package bth.imageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "bth.*")
public class BookTheHouseImageServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(BookTheHouseImageServiceApp.class, args);
    }
}
