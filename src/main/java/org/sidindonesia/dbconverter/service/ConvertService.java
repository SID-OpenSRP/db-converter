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
	private final DestinationDatabaseService destinationDatabaseService;

	@Scheduled(fixedRateString = "${scheduling.fixed-rate-in-ms}", initialDelayString = "${scheduling.initial-delay-in-ms}")
	public void convert() {
		// TODO change log level to debug
		log.info("Retrieving all required rows from Source DB...");
		Map<String, List<Map<String, Object>>> allRequiredTables = sourceDatabaseService.loadAll();

		log.info("Processing all required rows from Source DB to Destination DB...");
		destinationDatabaseService.processRowsFromSource(allRequiredTables);
	}
}
