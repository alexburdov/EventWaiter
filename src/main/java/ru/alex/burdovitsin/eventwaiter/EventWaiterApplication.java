package ru.alex.burdovitsin.eventwaiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class EventWaiterApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventWaiterApplication.class, args);
    }

}
