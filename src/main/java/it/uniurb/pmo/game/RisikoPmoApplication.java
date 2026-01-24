package it.uniurb.pmo.game;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "it.uniurb.pmo")
public class RisikoPmoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RisikoPmoApplication.class, args);
	}

}
