package org.sidindonesia.dbconverter.listener;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.sidindonesia.dbconverter.property.DestinationDatabaseProperties;
import org.sidindonesia.dbconverter.property.DestinationTable;
import org.sidindonesia.dbconverter.property.DestinationTable.DestinationColumn;
import org.sidindonesia.dbconverter.property.SourceDatabaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class DestinationDatabaseListener implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	@Qualifier("destinationJdbcOperations")
	private JdbcOperations destinationJdbcOperations;

	private final DestinationDatabaseProperties destinationDatabaseProperties;

	private final SourceDatabaseProperties sourceDatabaseProperties;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		log.info("Creating destination DB tables if not exists...");

		destinationJdbcOperations
			.execute("CREATE SCHEMA IF NOT EXISTS " + destinationDatabaseProperties.getSchemaName());

		List<String> ddlQueries = destinationDatabaseProperties.getTables().stream().map(table -> {
			String query = "CREATE TABLE IF NOT EXISTS ";
			query = query.concat(table.getName() + " (\n");

			List<String> columnDefinitions = table.getColumns().stream().map(column -> {
				String columnDefinition = column.getName().concat(" " + column.getTypeName());

				if (nonNull(column.getTypeLength())) {
					columnDefinition = columnDefinition.concat("(" + column.getTypeLength() + ")");
				}

				if (column.isUnique()) {
					columnDefinition = columnDefinition.concat(" UNIQUE");
				}

				return columnDefinition;
			}).collect(toList());

			String joinedColumnDefinitions = String.join(",\n", columnDefinitions);
			query = query.concat(joinedColumnDefinitions);

			query = query.concat("\n)");
			return query;
		}).collect(toList());
		String batchQuery = String.join(";\n\n", ddlQueries);
		batchQuery = batchQuery.concat(";");

		destinationJdbcOperations.execute(batchQuery);

		log.info("Destination DB migration completed.");

		syncLastIdOfEachSourceTables();
	}

	private void syncLastIdOfEachSourceTables() {
		sourceDatabaseProperties.getTables().parallelStream().forEach(sourceTable -> {
			DestinationTable destinationTable = destinationDatabaseProperties.getTables().stream()
				.filter(destTable -> destTable.getName().equals(sourceTable.getDestinationTableName())).findAny().get();

			String idColumnName = destinationTable.getColumns().stream()
				.filter(
					destinationColumn -> destinationColumn.getSourceColumnName().equals(sourceTable.getIdColumnName()))
				.map(DestinationColumn::getName).findAny().get();

			String query = "SELECT\n" + " CASE\n" + "  WHEN (\n" + "  SELECT\n" + "   COUNT(*)\n" + "  FROM\n" + "   "
				+ destinationTable.getName() + ")::varchar = '0' THEN '0'\n" + "  ELSE (\n" + "  SELECT\n" + "   "
				+ idColumnName + "\n" + "  FROM\n" + "   " + destinationTable.getName() + "\n" + "  ORDER BY\n" + "   "
				+ idColumnName + " DESC\n" + "  LIMIT 1)\n" + " END AS lastId";
			Object lastId = destinationJdbcOperations.queryForObject(query, Object.class);

			sourceTable.setLastId(lastId);
		});
	}
}
