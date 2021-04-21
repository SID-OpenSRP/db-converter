package org.sidindonesia.dbconverter.service;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class CustomRowMapper {
	public Map<String, Optional<Object>> mapRow(ResultSet rs, int rowNum) throws SQLException {
		return IntStream.rangeClosed(1, rs.getMetaData().getColumnCount()).mapToObj(i -> {
			Map<String, Optional<Object>> map = new HashMap<>();
			try {
				String columnName = rs.getMetaData().getColumnName(i);
				map.put(columnName, ofNullable(rs.getObject(columnName)));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return map;
		}).filter(map -> !map.isEmpty()).flatMap(map -> map.entrySet().stream())
			.collect(toMap(Entry::getKey, Entry::getValue));
	}
}
