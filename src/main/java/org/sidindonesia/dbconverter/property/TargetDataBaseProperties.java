package org.sidindonesia.dbconverter.property;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "target-database")
@Data
public class TargetDataBaseProperties {
	private List<String> tables;
}
