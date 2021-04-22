package org.sidindonesia.dbconverter.util;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SQLTypeUtil {
	/**
	 * Translates a data type from an integer (java.sql.Types value) to a string
	 * that represents the corresponding class.
	 * 
	 * REFER:
	 * https://www.cis.upenn.edu/~bcpierce/courses/629/jdkdocs/guide/jdbc/getstart/mapping.doc.html
	 * 
	 * @param type The java.sql.Types value to convert to its corresponding class.
	 * @return The class that corresponds to the given java.sql.Types value, or
	 *         Object.class if the type has no known mapping.
	 */
	public static Class<?> toClass(int type) {
		Class<?> result;

		switch (type) {
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
			result = String.class;
			break;

		case Types.NUMERIC:
		case Types.DECIMAL:
			result = java.math.BigDecimal.class;
			break;

		case Types.BIT:
			result = Boolean.class;
			break;

		case Types.TINYINT:
			result = Byte.class;
			break;

		case Types.SMALLINT:
			result = Short.class;
			break;

		case Types.INTEGER:
			result = Integer.class;
			break;

		case Types.BIGINT:
			result = Long.class;
			break;

		case Types.REAL:
		case Types.FLOAT:
			result = Float.class;
			break;

		case Types.DOUBLE:
			result = Double.class;
			break;

		case Types.BINARY:
		case Types.VARBINARY:
		case Types.LONGVARBINARY:
			result = Byte[].class;
			break;

		case Types.DATE:
			result = java.sql.Date.class;
			break;

		case Types.TIME:
			result = java.sql.Time.class;
			break;

		case Types.TIMESTAMP:
			result = java.sql.Timestamp.class;
			break;
		default:
			result = Object.class;
		}

		return result;
	}

	private static Object convertStringToAnotherType(String string, int type) {
		Object convertedObject;

		switch (type) {
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
			convertedObject = string;
			break;

		case Types.NUMERIC:
		case Types.DECIMAL:
			convertedObject = new BigDecimal(string);
			break;

		case Types.BIT:
			convertedObject = Boolean.valueOf(string);
			break;

		case Types.TINYINT:
			convertedObject = Byte.valueOf(string);
			break;

		case Types.SMALLINT:
			convertedObject = Short.valueOf(string);
			break;

		case Types.INTEGER:
			convertedObject = Integer.valueOf(string);
			break;

		case Types.BIGINT:
			convertedObject = Long.valueOf(string);
			break;

		case Types.REAL:
		case Types.FLOAT:
			convertedObject = Float.valueOf(string);
			break;

		case Types.DOUBLE:
			convertedObject = Double.valueOf(string);
			break;

		case Types.BINARY:
		case Types.VARBINARY:
		case Types.LONGVARBINARY:
			// TODO unimplemented
			convertedObject = string;
			break;

		case Types.DATE:
			convertedObject = java.sql.Date.valueOf(string);
			break;

		case Types.TIME:
			convertedObject = java.sql.Time.valueOf(string);
			break;

		case Types.TIMESTAMP_WITH_TIMEZONE:
			convertedObject = OffsetDateTime.from(ISO_DATE_TIME.parse(string));
			break;

		case Types.TIMESTAMP:
			convertedObject = LocalDateTime.from(ISO_DATE_TIME.parse(string));
			break;

		default:
			convertedObject = Object.class;
		}

		return convertedObject;
	}

	public static Object convertStringToAnotherType(String string, JDBCType jdbcType) {
		return convertStringToAnotherType(string, jdbcType.getVendorTypeNumber());
	}
}
