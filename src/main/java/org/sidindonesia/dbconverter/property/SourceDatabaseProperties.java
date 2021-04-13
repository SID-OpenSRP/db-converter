package org.sidindonesia.dbconverter.property;

import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "source-database")
@Data
public class SourceDatabaseProperties {
	private Set<Table> tables;
}
