package edu.upenn.cit594.datamanagement;

import java.io.File;
import java.nio.file.Files;
import java.util.Deque;
import java.util.Map;
import java.util.stream.Stream;

import edu.upenn.cit594.data.City;
import edu.upenn.cit594.data.Property;
import edu.upenn.cit594.data.ZipCode;
import edu.upenn.cit594.logging.Logger;

public class PropertiesReader {
	private String filename;
	private Logger logger;

	public PropertiesReader(String filename, Logger logger) {
		this.filename = filename;
		this.logger = logger;
	}

	public void readPropertiesData(City city) {
		Stream<String> linesStream = null;
		
		try {
			File fin = new File(this.filename);

			linesStream = Files.lines(fin.toPath());

			this.logger.write(this.filename); // log filename

			int[] indices = { -1, -1, -1 }; // stores indexes
											// indices[0] = index for zip_code
											// indices[1] = index for market_value
											// indices[2] = index for total_livable_area
			linesStream.forEach(line -> {
				// if indices haven't been determined, do that first
				if (indices[0] == -1) {
					extractIndicesFromHeader(line, indices);
				} else {
					String[] fields = parseLine(line, indices);
					try {
						String zipCode = fields[0].trim();
						if (zipCode.length() > 5) {
							zipCode = zipCode.substring(0, 5);
						}

						Map<String, ZipCode> zipCodesMap = city.getZipCodesMap();

						// if zipCode is within the city
						if (zipCodesMap.containsKey(zipCode)) {
							String marketValue = fields[1];
							String livableArea = fields[2];

							Property property = new Property(zipCode, livableArea, marketValue);

							Deque<Property> propertiesInZipCode = zipCodesMap.get(zipCode).getProperties();

							propertiesInZipCode.addFirst(property);
						}
					} catch (Exception e) {
						// read next line if any exception thrown
					}
				}
			});

			
			
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not read from properties data file: " + this.filename);
		} finally {
			try {
				linesStream.close();
			} catch (Exception e) {}
		}
	}

	private void extractIndicesFromHeader(String header, int[] indices) {
		// remove BOM if line starts with it.
		if (header.startsWith("\uFEFF")) {
			header = header.substring(1);
		}
		
		String[] headArr = header.split(",");

		for (int i = 0; i < headArr.length; i++) {
			String currentHeading = headArr[i];
			currentHeading = currentHeading.trim();
			if (indices[0] == -1 && currentHeading.equalsIgnoreCase("zip_code")) {
				indices[0] = i;
			} else if (indices[1] == -1 && currentHeading.equalsIgnoreCase("market_value")) {
				indices[1] = i;
			} else if (indices[2] == -1 && currentHeading.equalsIgnoreCase("total_livable_area")) {
				indices[2] = i;
			}
		}
	}

	private String[] parseLine(String line, int[] indices) {

		String[] fields = { null, null, null }; // to save zipcode, property value, area respectively
		int indexZipCode = indices[0];
		int indexValue = indices[1];
		int indexArea = indices[2];
		
		String currentField = null;
		int currentIndex = 0;
		boolean isWithinQuotes = false;

		// add a comma to end of the line to make sure the last column is parsed
		// correctly
		line = line + ",";
		int lineLen = line.length();
		char currentChar;
		for (int i = 0; i < lineLen; i++) {
			currentChar = line.charAt(i);

			if (!isWithinQuotes) { // if current char is not within quotes.

				// when encounter a comma, check if currentField is desired, and save it
				if (currentChar == ',') {
					if (currentIndex == indexZipCode) {

						fields[0] = currentField;
					} else if (currentIndex == indexValue) {

						fields[1] = currentField;
					} else if (currentIndex == indexArea) {

						fields[2] = currentField;
					}
					// reset current field
					currentField = null;

					// increment current index
					currentIndex++;
				} else if (currentChar == '"') {
					isWithinQuotes = true;
				} else {
					if (currentField == null) {
						currentField = Character.toString(currentChar);
					} else {
						currentField = currentField + currentChar;
					}
				}
			} else {
				if (currentChar == '"') {
					isWithinQuotes = false;
				}
			}
		}
		return fields;
	}
}
