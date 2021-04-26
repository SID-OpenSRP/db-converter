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
		private String jsonFilter;
		private String jsonPath;
		private boolean isUnique;

		// this was needed because lombok wrongly generates `setUnique` instead of
		// `setIsUnique`
		public void setIsUnique(boolean isUnique) {
			this.isUnique = isUnique;
		}
	}
}
