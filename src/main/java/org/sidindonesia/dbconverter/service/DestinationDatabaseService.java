package org.sidindonesia.dbconverter.service;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

import java.sql.JDBCType;
import java.util.List;
import java.util.Map;

import org.postgresql.util.PGobject;
import org.sidindonesia.dbconverter.property.DestinationDatabaseProperties;
import org.sidindonesia.dbconverter.property.DestinationTable.DestinationColumn;
import org.sidindonesia.dbconverter.util.SQLTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DestinationDatabaseService {
	private static final Configuration CONFIG = Configuration.builder()
		.options(Option.DEFAULT_PATH_LEAF_TO_NULL, Option.SUPPRESS_EXCEPTIONS).build();

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

						Object destinationColumnValue = JsonPath.parse(sourceColumnValue, CONFIG)
							.read(destinationColumn.getJsonPath());

						if (nonNull(destinationColumnValue)) {
							JDBCType jdbcType = JDBCType.valueOf(destinationColumn.getTypeName().toUpperCase()
								.replace(' ', '_').replace("TIME_ZONE", "TIMEZONE"));
							Object convertedValue = SQLTypeUtil.convertToAnotherType(destinationColumnValue, jdbcType);
							parameterSource.addValue(destinationColumn.getName(), convertedValue);
						} else {
							parameterSource.addValue(destinationColumn.getName(), destinationColumnValue);
						}

					} else {
						parameterSource.addValue(destinationColumn.getName(),
							sourceRow.get(destinationColumn.getSourceColumnName()));
					}
				});

				return parameterSource;

			}).toArray(MapSqlParameterSource[]::new);

			String query = "INSERT INTO " + destinationTable.getName() + " (\n";
			List<String> columnNames = destinationTable.getColumns().stream().map(DestinationColumn::getName)
				.collect(toList());
			query = query + String.join(", ", columnNames) + "\n) VALUES (\n";

			List<String> prependedColumnNames = columnNames.stream().map(columnName -> ":" + columnName)
				.collect(toList());
			query = query + String.join(", ", prependedColumnNames) + "\n)";

			return destinationJdbcTemplate.batchUpdate(query, parameterSources);
		}).collect(toList());
	}
}
