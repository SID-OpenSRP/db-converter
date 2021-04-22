package org.sidindonesia.dbconverter.property;

import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "destination-database")
@Data
public class DestinationDatabaseProperties {
	private String schemaName;
	private Set<DestinationTable> tables;
}
