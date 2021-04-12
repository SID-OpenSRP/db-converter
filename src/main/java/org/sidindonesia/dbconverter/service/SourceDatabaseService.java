package org.sidindonesia.dbconverter.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Set;

import org.sidindonesia.dbconverter.property.SourceDatabaseProperties;
import org.sidindonesia.dbconverter.property.SourceDatabaseProperties.Table;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SourceDatabaseService {
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private final SourceDatabaseProperties sourceDatabaseProperties;

	public List<List<Object>> loadAll() {
		Set<Table> requiredTables = sourceDatabaseProperties.getTables();

		return requiredTables.stream()
			.map(table -> namedParameterJdbcTemplate.query(
				"SELECT " + String.join(",", table.getColumns()) + " FROM " + table.getName() + " WHERE :id = 1",
				new MapSqlParameterSource("id", 1), (resultSet, rowNum) -> resultSet.getObject("id")))
			.collect(toList());
	}
}
