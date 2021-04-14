package org.sidindonesia.dbconverter.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.sidindonesia.dbconverter.property.TargetDatabaseProperties;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TargetDatabaseService {
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private final TargetDatabaseProperties targetDatabaseProperties;

	public void processRowsFromSource(Map<String, List<Map<String, Optional<Object>>>> allRequiredTables) {
		// TODO Auto-generated method stub

	}
}
