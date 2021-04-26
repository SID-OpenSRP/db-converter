package org.sidindonesia.dbconverter.listener;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.sidindonesia.dbconverter.property.DestinationDatabaseProperties;
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
	private JdbcOperations jdbcOperations;

	private final DestinationDatabaseProperties destinationDatabaseProperties;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		log.info("Creating destination DB tables if not exists...");

		jdbcOperations.execute("CREATE SCHEMA IF NOT EXISTS " + destinationDatabaseProperties.getSchemaName());

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

		jdbcOperations.execute(batchQuery);

		log.info("Destination DB migration completed.");
	}
}
