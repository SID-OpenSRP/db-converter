package org.sidindonesia.dbconverter.property;

import java.util.Set;

import lombok.Data;

@Data
public class TargetTable {
	private String name;
	private Set<TargetColumn> columns;
	private String query;

	@Data
	public static class TargetColumn {
		private String name;
		private String typeName;
		private Long typeLength;
		private String sourceColumnName;
		private String jsonPath;
		private String constraints;
	}
}
