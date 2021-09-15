package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.data.City;
import edu.upenn.cit594.logging.Logger;

public interface ParkingReader {
	public void readParkingData(City city);
	
	public static ParkingReader createWithGivenFormat(String format, String parkingfilename, Logger logger) {
		if (format.equalsIgnoreCase("csv")) {
			return new ParkingCSVReader(parkingfilename, logger);
		} else if (format.equalsIgnoreCase("json")) {
			return new ParkingJSONReader(parkingfilename, logger);
		}
		return null;
	}
}
