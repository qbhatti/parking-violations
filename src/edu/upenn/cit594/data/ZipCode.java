package edu.upenn.cit594.data;

import java.util.Deque;
import java.util.LinkedList;

public class ZipCode {
	private String zipCode;
	private int population;
	private Deque<ParkingViolation> parkingViolations;
	private Deque<Property> properties;
	
	//memoized fields
	private double totalFines = -1;
	private double totalPropertyValue = -1;
	private double averagePropertyValue = -1;
	private double totalLivableArea = -1;
	private double averageLivableArea = -1;
	private double valuePerCapita = -1;

	public ZipCode(String zipCode, int population) {
		this.population = population;
		this.zipCode = zipCode;
		this.parkingViolations = new LinkedList<>();
		this.properties = new LinkedList<>();
	}
	
	public String getZipCode() {
		return this.zipCode;
	}
	
	public int getPopulation() {
		return this.population;
	}
	
	public Deque<ParkingViolation> getParkingViolations(){
		return this.parkingViolations;
	}
	
	public void setParkingViolations(Deque<ParkingViolation> violations){
		this.parkingViolations = violations;
	}
	
	public Deque<Property> getProperties(){
		return this.properties;
	}
	
	public void setProperties(Deque<Property> properties){
		this.properties = properties;
	}
	
	public double getTotalFines() {
		return this.totalFines;
	}
	
	public void setTotalFines(double fines) {
		this.totalFines = fines;
	}
	
	public double getTotalPropertyValue() {
		return this.totalPropertyValue;
	}
	
	public double setTotalPropertyValue(double val) {
		return this.totalPropertyValue = val;
	}
	
	public double getAveragePropertyValue() {
		return this.averagePropertyValue;
	}
	
	public double setAveragePropertyValue(double val) {
		return this.averagePropertyValue = val;
	} 
	
	public double getTotalLivableArea() {
		return this.totalLivableArea;
	}
	
	public double setTotalLivableArea(double val) {
		return this.totalLivableArea = val;
	}
	
	public double getAverageLivableArea() {
		return this.averageLivableArea;
	}
	
	public double setAverageLivableArea(double val) {
		return this.averageLivableArea = val;
	} 
	
	public double getValuePerCapita() {
		return this.valuePerCapita;
	}
	
	public double setValuePerCapita(double val) {
		return this.valuePerCapita = val;
	} 
}
