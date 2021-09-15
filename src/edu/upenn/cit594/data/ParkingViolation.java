package edu.upenn.cit594.data;

public class ParkingViolation {
	private String timeStamp;
	private double fineAmount;
	private String description;
	private String vehicleID;
	private String regState;
	private String violationID;
	private String zipCode;
	
	public ParkingViolation(String timeStamp, double fineAmount, String description, String vehicleID, String regState, String violationID, String zipCode) {
		this.timeStamp = timeStamp;
		this.fineAmount = fineAmount;
		this.description = description;
		this.vehicleID = vehicleID;
		this.regState = regState;
		this.violationID = violationID;
		this.zipCode = zipCode;
	}
	
	public double getFineAmount() {
		return fineAmount;
	}
	
	public String getTimeStamp() {
		return timeStamp;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getVehicleID() {
		return vehicleID;
	}
	
	public String getPlateState() {
		return regState;
	}
	
	public String getViolationID() {
		return violationID;
	}
	
	public String getZipCode() {
		return zipCode;
	}
}
