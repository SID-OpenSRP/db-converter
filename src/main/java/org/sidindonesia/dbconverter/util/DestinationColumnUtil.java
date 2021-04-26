package org.sidindonesia.dbconverter.util;

import static java.util.Objects.nonNull;

import java.sql.JDBCType;

import org.postgresql.util.PGobject;
import org.sidindonesia.dbconverter.property.DestinationTable.DestinationColumn;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DestinationColumnUtil {
	private static final Configuration CONFIG = Configuration.builder()
		.options(Option.DEFAULT_PATH_LEAF_TO_NULL, Option.SUPPRESS_EXCEPTIONS).build();

	public static Object mapValue(DestinationColumn destinationColumn, Object sourceColumnValue) {
		if (nonNull(destinationColumn.getJsonPath()) && nonNull(sourceColumnValue)) {
			PGobject sourceColumnValueAsPGobject = (PGobject) sourceColumnValue;
			String pgObjectValue = sourceColumnValueAsPGobject.getValue();

			return jsonPathEvaluation(destinationColumn, pgObjectValue);
		} else {
			return sourceColumnValue;
		}
	}

	private static Object jsonPathEvaluation(DestinationColumn destinationColumn, String pgObjectValue) {
		Object jsonPathEvaluation = applyFilter(destinationColumn, pgObjectValue);

		if (nonNull(jsonPathEvaluation)) {
			JDBCType jdbcType = JDBCType.valueOf(
				destinationColumn.getTypeName().toUpperCase().replace(' ', '_').replace("TIME_ZONE", "TIMEZONE"));
			return SQLTypeUtil.convertToAnotherType(jsonPathEvaluation, jdbcType);
		} else {
			return jsonPathEvaluation;
		}
	}

	private static Object applyFilter(DestinationColumn destinationColumn, String pgObjectValue) {
		if (nonNull(destinationColumn.getJsonFilter())) {
			Object filteredValues = JsonPath.parse(pgObjectValue, CONFIG).read(destinationColumn.getJsonFilter());
			return JsonPath.parse(filteredValues, CONFIG).read(destinationColumn.getJsonPath());
		} else {
			return JsonPath.parse(pgObjectValue, CONFIG).read(destinationColumn.getJsonPath());
		}
	}
}
