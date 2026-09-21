package eu.bilch.timetables;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TimetablesApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimetablesApplication.class, args);
	}

}
