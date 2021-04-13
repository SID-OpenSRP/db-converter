package org.sidindonesia.dbconverter.property;

import java.util.Set;

import lombok.Data;

@Data
public class Table {
	private String name;
	private Set<String> columns;
}