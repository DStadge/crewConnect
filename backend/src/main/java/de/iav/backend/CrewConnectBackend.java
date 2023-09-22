package de.iav.backend;

import de.iav.backend.service.SailorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrewConnectBackend {

	private static final Logger logger = LoggerFactory.getLogger(SailorService.class);

	public static void main(String[] args) {
		SpringApplication.run(CrewConnectBackend.class, args);
		//logger.info("CrewConnectBackend started");
		logger.debug("CrewConnectBackend started");
	}

}
