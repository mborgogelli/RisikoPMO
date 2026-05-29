package it.uniurb.pmo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

// Classe principale dell'applicazione Spring Boot. Contiene il metodo main che avvia l'applicazione.
@SpringBootApplication
// Indica a Spring di scansionare il pacchetto "it.uniurb.pmo" per trovare i componenti, i servizi e i controller da gestire.
public class RisikoPmoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RisikoPmoApplication.class, args);
	}

}
