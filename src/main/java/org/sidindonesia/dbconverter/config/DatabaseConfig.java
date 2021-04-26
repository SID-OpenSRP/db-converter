package org.sidindonesia.dbconverter.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DatabaseConfig {
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource sourceDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.destination-datasource")
	public DataSource destinationDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public ColumnMapRowMapper columnMapRowMapper() {
		return new ColumnMapRowMapper();
	}

	@Primary
	@Bean
	public NamedParameterJdbcTemplate sourceJdbcTemplate(DataSource sourceDataSource) {
		return new NamedParameterJdbcTemplate(sourceDataSource);
	}

	@Bean
	public NamedParameterJdbcTemplate destinationJdbcTemplate(
		@Qualifier("destinationDataSource") DataSource destinationDataSource) {
		return new NamedParameterJdbcTemplate(destinationDataSource);
	}

	@Primary
	@Bean
	public JdbcOperations sourceJdbcOperations(
		@Qualifier("sourceJdbcTemplate") NamedParameterJdbcTemplate sourceJdbcTemplate) {
		return sourceJdbcTemplate.getJdbcOperations();
	}

	@Bean
	public JdbcOperations destinationJdbcOperations(
		@Qualifier("destinationJdbcTemplate") NamedParameterJdbcTemplate destinationJdbcTemplate) {
		return destinationJdbcTemplate.getJdbcOperations();
	}
}
