package org.sidindonesia.dbconverter.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.ColumnMapRowMapper;

@Configuration
public class DatabaseConfig {
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource sourceDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.target-datasource")
	public DataSource targetDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public ColumnMapRowMapper columnMapRowMapper() {
		return new ColumnMapRowMapper();
	}
}
