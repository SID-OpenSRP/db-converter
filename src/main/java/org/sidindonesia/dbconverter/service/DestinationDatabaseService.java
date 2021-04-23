package org.sidindonesia.dbconverter.service;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

import java.sql.JDBCType;
import java.util.List;
import java.util.Map;

import org.postgresql.util.PGobject;
import org.sidindonesia.dbconverter.property.DestinationDatabaseProperties;
import org.sidindonesia.dbconverter.util.SQLTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.JsonPath;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DestinationDatabaseService {
	@Autowired
	@Qualifier("destinationJdbcTemplate")
	private NamedParameterJdbcTemplate destinationJdbcTemplate;

	private final DestinationDatabaseProperties destinationDatabaseProperties;

	public List<int[]> processRowsFromSource(Map<String, List<Map<String, Object>>> allRequiredTables) {
		return destinationDatabaseProperties.getTables().stream().map(destinationTable -> {
			List<Map<String, Object>> sourceResultList = allRequiredTables.get(destinationTable.getName());

			MapSqlParameterSource[] parameterSources = sourceResultList.stream().map(sourceRow -> {
				MapSqlParameterSource parameterSource = new MapSqlParameterSource();

				destinationTable.getColumns().forEach(destinationColumn -> {
					if (nonNull(destinationColumn.getJsonPath())) {
						PGobject sourceColumnPGObjectValue = (PGobject) sourceRow
							.get(destinationColumn.getSourceColumnName());
						String sourceColumnValue = sourceColumnPGObjectValue.getValue();

						String destinationColumnValue = JsonPath.parse(sourceColumnValue)
							.read(destinationColumn.getJsonPath());

						JDBCType jdbcType = JDBCType.valueOf(destinationColumn.getTypeName().toUpperCase()
							.replace(' ', '_').replace("TIME_ZONE", "TIMEZONE"));
						Object convertedValue = SQLTypeUtil.convertStringToAnotherType(destinationColumnValue,
							jdbcType);

						parameterSource.addValue(destinationColumn.getName(), convertedValue);
					} else {
						parameterSource.addValue(destinationColumn.getName(),
							sourceRow.get(destinationColumn.getSourceColumnName()));
					}
				});

				return parameterSource;

			}).toArray(MapSqlParameterSource[]::new);

			String query = "INSERT INTO " + destinationTable.getName() + " (\n";
			List<String> columnNames = destinationTable.getColumns().stream().map(destinationColumn -> {
				return destinationColumn.getName();
			}).collect(toList());
			query = query + String.join(", ", columnNames) + "\n) VALUES (\n";

			List<String> prependedColumnNames = columnNames.stream().map(columnName -> ":" + columnName)
				.collect(toList());
			query = query + String.join(", ", prependedColumnNames) + "\n)";

			return destinationJdbcTemplate.batchUpdate(query, parameterSources);
		}).collect(toList());
	}
}
