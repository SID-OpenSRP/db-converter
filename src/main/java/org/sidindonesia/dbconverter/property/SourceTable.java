package org.sidindonesia.dbconverter.property;

import lombok.Data;

@Data
public class SourceTable {
	private String targetTableName;
	private String name;
	private String query;
	private String idColumnName;
	private Object lastId;
}