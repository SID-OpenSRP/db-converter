package org.sidindonesia.dbconverter.service;

import java.util.List;
import java.util.Map;

import org.sidindonesia.dbconverter.property.TargetDatabaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TargetDatabaseService {
	@Autowired
	@Qualifier("targetJdbcTemplate")
	private NamedParameterJdbcTemplate targetJdbcTemplate;

	private final TargetDatabaseProperties targetDatabaseProperties;

	public void processRowsFromSource(Map<String, List<Map<String, Object>>> allRequiredTables) {
		// TODO Auto-generated method stub
	}
}
