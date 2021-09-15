package edu.upenn.cit594.datamanagement;

import java.io.File;
import java.util.Deque;
import java.util.Map;
import java.util.Scanner;

import edu.upenn.cit594.data.City;
import edu.upenn.cit594.data.ParkingViolation;
import edu.upenn.cit594.data.ZipCode;
import edu.upenn.cit594.logging.Logger;

public class ParkingCSVReader implements ParkingReader {

	private String filename;
	private Logger logger;

	public ParkingCSVReader(String filename, Logger logger) {
		this.filename = filename;
		this.logger = logger;
	}

	// reads input file for data on parking violation. Any data entry with unknown
	// zipcode, license plate out of state "PA" is ignored
	public void readParkingData(City city) {
		
		Scanner in = null;

		try {
			in = new Scanner(new File(this.filename));
			
			this.logger.write(this.filename); // log filename 
			
			while (in.hasNextLine()) {
				String line = in.nextLine().trim();
				String[] fields = line.split(",");

				try {
					String timeStamp = fields[0].trim();
					double fineAmount = Double.parseDouble(fields[1].trim());
					String description = fields[2].trim();
					String vehicleID = fields[3].trim();
					String regState = fields[4].trim();
					String violationID = fields[5].trim();
					String zipCode = fields[6].trim();

					Map<String, ZipCode> cityZipCodesMap = city.getZipCodesMap();
					String citysState = city.getState();
					
					//if zipcode is within the city, add violation.
					if(regState.equalsIgnoreCase(citysState) && cityZipCodesMap.containsKey(zipCode)) {
						ParkingViolation violation = new ParkingViolation(timeStamp, fineAmount, description, vehicleID,
								regState, violationID, zipCode);
						
						Deque<ParkingViolation> violationsList = cityZipCodesMap.get(zipCode).getParkingViolations();
						
						violationsList.addFirst(violation);
					}
				} catch (Exception e) {
					// read next line if any exception thrown, e.g., fine amount is not a number
				}
			}
			
		} catch (Exception e) { // any other exceptions like IOException
			throw new IllegalArgumentException("Could not open parking violations file: " + this.filename);
		} finally {
			try {
				in.close();
			} catch (Exception e) {}
			
		}
	}
}
