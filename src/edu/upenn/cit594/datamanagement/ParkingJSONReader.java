package edu.upenn.cit594.datamanagement;

import java.io.FileReader;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import edu.upenn.cit594.data.City;
import edu.upenn.cit594.data.ParkingViolation;
import edu.upenn.cit594.data.ZipCode;
import edu.upenn.cit594.logging.Logger;

public class ParkingJSONReader implements ParkingReader {
	private String filename;
	private Logger logger;

	public ParkingJSONReader(String filename, Logger logger) {
		this.filename = filename;
		this.logger = logger;
	}

	public void readParkingData(City city) {

		try {
			JSONParser parser = new JSONParser();
			JSONArray jsonViolationsArray = (JSONArray) parser.parse(new FileReader(this.filename));

			this.logger.write(this.filename); // log filename
			
			Iterator<?> iter = jsonViolationsArray.iterator();

			while (iter.hasNext()) {

				try {
					JSONObject jsonViolationObj = (JSONObject) iter.next();

					String timeStamp = (String) jsonViolationObj.get("date");
					long fineAmount = (long) jsonViolationObj.get("fine");
					String description = (String) jsonViolationObj.get("violation");
					String vehicleID = (String) jsonViolationObj.get("palte_id");
					String regState = (String) jsonViolationObj.get("state");
					String violationID = jsonViolationObj.get("ticket_number").toString();
					String zipCode = (String) jsonViolationObj.get("zip_code");
					if (zipCode.trim().length() == 5) {
						Map<String, ZipCode> cityZipCodesMap = city.getZipCodesMap();
						String citysState = city.getState();
						//if zipcode is within the city, add violation.
						if(regState.equalsIgnoreCase(citysState) && cityZipCodesMap.containsKey(zipCode)) {
							ParkingViolation violation = new ParkingViolation(timeStamp, fineAmount, description, vehicleID,
									regState, violationID, zipCode);
							
							Deque<ParkingViolation> violationsList = cityZipCodesMap.get(zipCode).getParkingViolations();
							
							violationsList.addFirst(violation);
						}
					}
				} catch (Exception e) {
					// ignore exception, read next line
				}
			}
			
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not open parking violations file: " + this.filename);
		}
	}
}
