package org.sidindonesia.dbconverter.service;

import static java.util.stream.Collectors.toList;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.jayway.jsonpath.JsonPath;

class GenerateConfigTest {
	@Test
	void testGenerateConfigProperties() throws Exception {
		String jsonContent = Files.readString(Path.of("D:\\SourceCodes\\SID\\tes.json"));
		Object jsonArray = JsonPath.parse(jsonContent).read("$.obs[*].formSubmissionField");
		String jsonArrayString = String.valueOf(jsonArray);
		String strings = jsonArrayString.substring(1, jsonArrayString.length() - 2).replace("\"", "");
		String[] arrayOfString = strings.split(",");

		List<String> formSubmissionFields = List.of(arrayOfString);

		List<String> configProperties = formSubmissionFields.stream().map(formSubmissionField -> {
			String configProperty = "    - name: " + formSubmissionField + "\n" + "      typeName: varchar\n"
				+ "      sourceColumnName: json\n" + "      jsonFilter: $.obs[?(@.formSubmissionField == '"
				+ formSubmissionField + "')].values[0]\n" + "      jsonPath: $[0]\n";
			return configProperty;
		}).collect(toList());

		String configString = configProperties.toString().substring(1, configProperties.toString().length() - 2)
			.replace(", ", "");

		Files.deleteIfExists(Path.of("D:\\SourceCodes\\SID\\test_generated.yml"));
		Files.writeString(Files.createFile(Path.of("D:\\SourceCodes\\SID\\test_generated.yml")), configString);
		System.out.println(configString);
	}
}
