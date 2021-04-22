package org.sidindonesia.dbconverter.property;

import java.util.Set;

import lombok.Data;

@Data
public class DestinationTable {
	private String name;
	private Set<DestinationColumn> columns;
	private String query;

	@Data
	public static class DestinationColumn {
		private String name;
		private String typeName;
		private Long typeLength;
		private String sourceColumnName;
		private String jsonPath;
		private String constraints;
	}
}
