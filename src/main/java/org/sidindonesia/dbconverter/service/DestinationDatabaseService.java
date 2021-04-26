package org.sidindonesia.dbconverter.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;

import org.sidindonesia.dbconverter.property.DestinationDatabaseProperties;
import org.sidindonesia.dbconverter.property.DestinationTable.DestinationColumn;
import org.sidindonesia.dbconverter.util.DestinationColumnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

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
					Object sourceColumnValue = sourceRow.get(destinationColumn.getSourceColumnName());
					Object destinationColumnValue = DestinationColumnUtil.mapValue(destinationColumn,
						sourceColumnValue);

					parameterSource.addValue(destinationColumn.getName(), destinationColumnValue);
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
