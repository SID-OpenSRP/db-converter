package org.sidindonesia.dbconverter.service;

import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ConvertService {
	private final SourceDatabaseService sourceDatabaseService;
	private final TargetDatabaseService targetDatabaseService;

	@Scheduled(fixedRateString = "${fixedRate.in.milliseconds}")
	public void convert() {
		log.info("Retrieving all required rows from Source DB...");
		Map<String, List<Map<String, Object>>> allRequiredTables = sourceDatabaseService.loadAll();

		log.info("Processing all required rows from Source DB to Target DB...");
		targetDatabaseService.processRowsFromSource(allRequiredTables);
	}
}
