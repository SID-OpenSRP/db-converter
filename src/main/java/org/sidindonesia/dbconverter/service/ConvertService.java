package org.sidindonesia.dbconverter.service;

import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConvertService {
	private final SourceDatabaseService sourceDatabaseService;
	private final TargetDatabaseService targetDatabaseService;

	@Scheduled(fixedRateString = "${fixedRate.in.milliseconds}")
	public void convertSchemaCoreToSid() {
		log.debug("Retrieving all required rows from Source DB...");
		Map<String, List<Map<String, Object>>> allRequiredTables = sourceDatabaseService.loadAll();

		log.debug("Processing all required rows from Source DB to Target DB...");
		targetDatabaseService.processRowsFromSource(allRequiredTables);
	}
}
