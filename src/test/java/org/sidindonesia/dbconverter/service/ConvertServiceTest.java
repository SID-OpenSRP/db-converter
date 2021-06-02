package org.sidindonesia.dbconverter.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.sidindonesia.dbconverter.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

class ConvertServiceTest extends IntegrationTest {
	@Autowired
	private ConvertService convertService;

	@Test
	void testConvert() throws Exception {
		convertService.convert();
		assertThat(convertService).isNotNull();
	}
}
