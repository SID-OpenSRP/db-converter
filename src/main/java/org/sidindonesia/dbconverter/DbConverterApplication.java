package org.sidindonesia.dbconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DbConverterApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbConverterApplication.class, args);
	}

}
