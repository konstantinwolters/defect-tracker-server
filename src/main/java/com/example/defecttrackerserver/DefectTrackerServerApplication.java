package com.example.defecttrackerserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DefectTrackerServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DefectTrackerServerApplication.class, args);
    }

    //TODO: Check if all bidirectional relationships are set up correctly. Meaning that on creation or changes both sides are updated accordingly

}
