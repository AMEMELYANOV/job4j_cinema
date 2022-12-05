package ru.job4j.cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CinemaApplication {

     public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
        System.out.println("Go to http://localhost:8080/index");
    }
}
