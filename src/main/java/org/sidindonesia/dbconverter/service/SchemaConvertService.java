package org.sidindonesia.opensrpschemaconvert.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SchemaConvertService {
	@Scheduled(fixedRateString = "${fixedRate.in.milliseconds}")
	public void convertSchemaCoreToSid() {
		log.info("test");
	}
}
