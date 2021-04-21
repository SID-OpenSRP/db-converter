package org.sidindonesia.dbconverter.property;

import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "target-database")
@Data
public class TargetDatabaseProperties {
	private String schemaName;
	private Set<TargetTable> tables;
}
