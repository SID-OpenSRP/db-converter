package org.sidindonesia.dbconverter;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class DbConverterApplicationTests {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DataSource sourceDataSource;

	@Autowired
	@Qualifier("targetDataSource")
	private DataSource targetDataSource;

	@Value("${spring.datasource.username}")
	private String sourceDataSourceUserName;

	@Value("${spring.target-datasource.username}")
	private String targetDataSourceUserName;

	@Test
	void contextLoads() {
	}

	@Test
	void testChangeDataSource_assertThatSourceDataSourceIsPrimary() throws Exception {
		assertThat(jdbcTemplate.getDataSource()).isEqualTo(sourceDataSource);
		assertThat(jdbcTemplate.getDataSource().getConnection().getMetaData().getUserName())
			.isEqualTo(sourceDataSourceUserName);

		jdbcTemplate.setDataSource(targetDataSource);

		assertThat(jdbcTemplate.getDataSource()).isEqualTo(targetDataSource);
		assertThat(jdbcTemplate.getDataSource().getConnection().getMetaData().getUserName())
			.isEqualTo(targetDataSourceUserName);
	}

}
