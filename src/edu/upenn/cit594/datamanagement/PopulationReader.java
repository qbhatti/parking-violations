package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.FileReader;

import edu.upenn.cit594.data.City;
import edu.upenn.cit594.data.ZipCode;
import edu.upenn.cit594.logging.Logger;

public class PopulationReader {
	private String filename;
	private Logger logger;
	
	public PopulationReader(String filename, Logger logger) {
		this.filename = filename;
		this.logger = logger;
	}

	public void readCityPopulationData(City city) {
		
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(filename);
			bufferedReader = new BufferedReader(fileReader);
			
			this.logger.write(filename); // log filename

			String line;

			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				if (line.length() > 0) {
					try {
						int indexOfSpaceChar = line.indexOf(" ");
						String zipCode = line.substring(0, indexOfSpaceChar);
						int population = Integer.parseInt(line.substring(indexOfSpaceChar + 1));

						if (zipCode.length() == 5) {
							city.addZipCode(new ZipCode(zipCode, population));
						}
					} catch (Exception e) {
						// ignore exception and read next line
					}
				}
			}

		} catch (Exception e) { // any other exceptions like IOException
			throw new IllegalArgumentException("Could not open population data file: " + this.filename);
		} finally {
			try {
				fileReader.close();
				bufferedReader.close();
			} catch (Exception e) {
				// ignore exception
			}
		}
	}
}