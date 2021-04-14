package org.sidindonesia.dbconverter.property;

import lombok.Data;

@Data
public class Table {
	private String name;
	private String query;
	private String idColumnName;
	private Object lastId;
}