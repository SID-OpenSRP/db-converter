package org.sidindonesia.dbconverter.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;

import org.postgresql.util.PGobject;
import org.sidindonesia.dbconverter.property.TargetDatabaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.JsonPath;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TargetDatabaseService {
	@Autowired
	@Qualifier("targetJdbcTemplate")
	private NamedParameterJdbcTemplate targetJdbcTemplate;

	private final TargetDatabaseProperties targetDatabaseProperties;

	public List<int[]> processRowsFromSource(Map<String, List<Map<String, Object>>> allRequiredTables) {
		return targetDatabaseProperties.getTables().stream().map(targetTable -> {
			List<Map<String, Object>> sourceResultList = allRequiredTables.get(targetTable.getName());

			MapSqlParameterSource[] parameterSources = sourceResultList.stream().map(sourceRow -> {
				MapSqlParameterSource parameterSource = new MapSqlParameterSource();

				targetTable.getColumns().forEach(targetColumn -> {
					if (targetColumn.getSourceColumnType().equalsIgnoreCase("json")) {
						PGobject sourceColumnPGObjectValue = (PGobject) sourceRow
							.get(targetColumn.getSourceColumnName());
						String sourceColumnValue = sourceColumnPGObjectValue.getValue();

						Object targetColumnValue = JsonPath.parse(sourceColumnValue).read(targetColumn.getJsonPath());

						parameterSource.addValue(targetColumn.getName(), targetColumnValue);
					} else {
						parameterSource.addValue(targetColumn.getName(),
							sourceRow.get(targetColumn.getSourceColumnName()));
					}
				});

				return parameterSource;

			}).toArray(MapSqlParameterSource[]::new);

			return targetJdbcTemplate.batchUpdate(targetTable.getQuery(), parameterSources);
		}).collect(toList());
	}
}
