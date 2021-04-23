package org.sidindonesia.dbconverter;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(properties = "scheduling.enabled=true")
class DbConverterApplicationTests extends IntegrationTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	@Qualifier("destinationClassicJdbcTemplate")
	private JdbcTemplate destinationClassicJdbcTemplate;

	@Autowired
	private DataSource sourceDataSource;

	@Autowired
	@Qualifier("destinationDataSource")
	private DataSource destinationDataSource;

	@Value("${spring.datasource.username}")
	private String sourceDataSourceUserName;

	@Value("${spring.destination-datasource.username}")
	private String destinationDataSourceUserName;

	@Value("${destination-database.schemaName}")
	private String destinationDatabaseSchemaName;

	@BeforeEach
	void setUp() {
		destinationClassicJdbcTemplate.execute("DROP SCHEMA IF EXISTS " + destinationDatabaseSchemaName + " CASCADE");
		destinationClassicJdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS " + destinationDatabaseSchemaName);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void contextLoads() {
	}

	@Test
	void testChangeDataSource_assertThatSourceDataSourceIsPrimary() throws Exception {
		assertThat(jdbcTemplate.getDataSource()).isEqualTo(sourceDataSource);
		assertThat(jdbcTemplate.getDataSource().getConnection().getMetaData().getUserName())
			.isEqualTo(sourceDataSourceUserName);

		jdbcTemplate.setDataSource(destinationDataSource);

		assertThat(jdbcTemplate.getDataSource()).isEqualTo(destinationDataSource);
		assertThat(jdbcTemplate.getDataSource().getConnection().getMetaData().getUserName())
			.isEqualTo(destinationDataSourceUserName);
	}

}
