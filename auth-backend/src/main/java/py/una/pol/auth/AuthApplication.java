package py.una.pol.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Authentication service.
 * Responsible for initializing the Spring Boot application.
 */
@SpringBootApplication
public class AuthApplication {
	/**
	 * Main method that serves as the entry point of the Spring Boot application.
	 *
	 * @param args command-line arguments passed during application startup.
	 */
	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

}
	