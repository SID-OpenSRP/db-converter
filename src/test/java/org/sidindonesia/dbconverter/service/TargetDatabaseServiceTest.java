package org.sidindonesia.dbconverter.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.sidindonesia.dbconverter.IntegrationTest;
import org.sidindonesia.dbconverter.property.TargetDatabaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

class TargetDatabaseServiceTest extends IntegrationTest {
	@Autowired
	private TargetDatabaseService targetDatabaseService;

	@Autowired
	private TargetDatabaseProperties targetDatabaseProperties;

	@Autowired
	@Qualifier("targetJdbcTemplate")
	private NamedParameterJdbcTemplate targetJdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate sourceJdbcTemplate;

	@Test
	void assertThatSourceJdbcTemplateIsNotEqualToTargetJdbcTemplate() throws Exception {
		assertThat(sourceJdbcTemplate).isNotEqualTo(targetJdbcTemplate);
	}
}
