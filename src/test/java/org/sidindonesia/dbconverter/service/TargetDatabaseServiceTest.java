package org.sidindonesia.dbconverter.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.sidindonesia.dbconverter.IntegrationTest;
import org.sidindonesia.dbconverter.property.DestinationDatabaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

class DestinationDatabaseServiceTest extends IntegrationTest {
	@Autowired
	private DestinationDatabaseService destinationDatabaseService;

	@Autowired
	private DestinationDatabaseProperties destinationDatabaseProperties;

	@Autowired
	@Qualifier("destinationJdbcTemplate")
	private NamedParameterJdbcTemplate destinationJdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate sourceJdbcTemplate;

	@Test
	void assertThatSourceJdbcTemplateIsNotEqualToDestinationJdbcTemplate() throws Exception {
		assertThat(sourceJdbcTemplate).isNotEqualTo(destinationJdbcTemplate);
	}
}
