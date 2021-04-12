package org.sidindonesia.dbconverter.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SourceDatabaseServiceTest {
	@Autowired
	private SourceDatabaseService sourceDatabaseService;

	@Test
	void testLoadAll() throws Exception {
		List<List<Object>> allResultSetJson = sourceDatabaseService.loadAll();
		assertThat(allResultSetJson).isNotEmpty();
	}
}
