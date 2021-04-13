package org.sidindonesia.dbconverter.property;

import lombok.Data;

@Data
public class Table {
	private String name;
	private Object lastId;
	private String query;
}