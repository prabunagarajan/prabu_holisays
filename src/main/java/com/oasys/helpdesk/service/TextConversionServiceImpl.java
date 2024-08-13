package com.oasys.helpdesk.service;

import org.springframework.stereotype.Service;

@Service
public class TextConversionServiceImpl implements TextConversionService {

	@Override
	public String convertToJSON(String textDetails) {
		// Assuming textDetails is a string representation of JSON-like data
		// Parse the string and return the JSON object
		// You can use libraries like Jackson or Gson for JSON parsing

		// Here's a simple example without any external library
		// Please note that this implementation might not cover all edge cases
		String[] keyValuePairs = textDetails.split(",\\s*");
		StringBuilder jsonBuilder = new StringBuilder("{");
		for (String pair : keyValuePairs) {
			String[] entry = pair.split("=");
			jsonBuilder.append("\"").append(entry[0]).append("\":\"");
			if (entry.length > 1) {
				jsonBuilder.append(entry[1]);
			}
			jsonBuilder.append("\",");
		}
		jsonBuilder.deleteCharAt(jsonBuilder.length() - 1); // Remove the trailing comma
		jsonBuilder.append("}");
		return jsonBuilder.toString();
	}
}
