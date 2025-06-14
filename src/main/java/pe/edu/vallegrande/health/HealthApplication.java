package pe.edu.vallegrande.health;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class HealthApplication {

	public static void main(String[] args) {
		// Cargar variables desde archivo .env
		Dotenv dotenv = Dotenv.configure()
				.directory(".")
				.ignoreIfMalformed()
				.ignoreIfMissing()
				.load();

		dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
		);
		SpringApplication.run(HealthApplication.class, args);
	}

}
