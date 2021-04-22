package org.sidindonesia.dbconverter;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.junit.jupiter.api.Disabled;
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
	private DataSource sourceDataSource;

	@Autowired
	@Qualifier("destinationDataSource")
	private DataSource destinationDataSource;

	@Value("${spring.datasource.username}")
	private String sourceDataSourceUserName;

	@Value("${spring.destination-datasource.username}")
	private String destinationDataSourceUserName;

	@Test
	void contextLoads() {
	}

	@Disabled("Affects other integration tests")
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
