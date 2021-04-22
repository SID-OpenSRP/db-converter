package org.sidindonesia.dbconverter.listener;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.sidindonesia.dbconverter.property.TargetDatabaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class TargetDatabaseListener implements ApplicationListener<ApplicationReadyEvent> {
	@Autowired
	@Qualifier("targetJdbcTemplate")
	private NamedParameterJdbcTemplate targetJdbcTemplate;

	private final TargetDatabaseProperties targetDatabaseProperties;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		log.info("Creating target DB tables if not exists...");

		JdbcTemplate jdbcTemplate = targetJdbcTemplate.getJdbcTemplate();
		jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS " + targetDatabaseProperties.getSchemaName());

		List<String> ddlQueries = targetDatabaseProperties.getTables().stream().map(table -> {
			String query = "CREATE TABLE IF NOT EXISTS ";
			query = query.concat(table.getName() + " (\n");

			List<String> columnDefinitions = table.getColumns().stream().map(column -> {
				String columnDefinition = column.getName().concat(" " + column.getTypeName());

				if (nonNull(column.getTypeLength())) {
					columnDefinition = columnDefinition.concat("(" + column.getTypeLength() + ")");
				}

				if (nonNull(column.getConstraints())) {
					columnDefinition = columnDefinition.concat(" " + column.getConstraints());
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

		jdbcTemplate.execute(batchQuery);
	}
}
