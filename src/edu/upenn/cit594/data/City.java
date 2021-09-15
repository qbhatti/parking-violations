package edu.upenn.cit594.data;


import java.util.HashMap;
import java.util.Map;


public class City {
	private String name;
	private String state;
	private Map<String, ZipCode> zipCodesMap;
	
	//flags
	private boolean isPopulationDataAvailable = false;
	private boolean isPropertyDataAvailable = false;
	private boolean isParkingDataAvailable = false;
	
	
	private int totalPopulation = -1;
	private Map<String, Double> finesPerCapita = null;

	public City(String name, String state) {
		this.name = name;
		this.state = state;
	}
	
	public String getState() {
		return this.state;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getTotalPopulation() {
		return this.totalPopulation;
	}
	
	public void setTotalPopulation(int totalPopulation) {
		this.totalPopulation = totalPopulation > 0 ? totalPopulation: 0;
	}
	
	public Map<String, ZipCode> getZipCodesMap(){
		return this.zipCodesMap;
	}
	
	public ZipCode getZipCode(String zipCode) {
		if(this.zipCodesMap.containsKey(zipCode)) {
			return zipCodesMap.get(zipCode);
		}
		return null;
	}
	
	public boolean getIsPopulationDataAvailable() {
		return this.isPopulationDataAvailable;
	}
	
	public void setIsPopulationDataAvailable(boolean val) {
		this.isPopulationDataAvailable = val;
	}
	
	public boolean getIsPropertyDataAvailable() {
		return this.isPropertyDataAvailable;
	}
	
	public void setIsPropertyDataAvailable(boolean val) {
		this.isPropertyDataAvailable = val;
	}
	
	public boolean getIsParkingDataAvailable() {
		return this.isParkingDataAvailable;
	}
	
	public void setIsParkingDataAvailable(boolean val) {
		this.isParkingDataAvailable = val;
	}
	
	public void setFinesPerCapita(Map<String, Double> finesPerCapitaMap) {
		this.finesPerCapita = finesPerCapitaMap;
	}
	
	public Map<String, Double> getFinesPerCapita(){
		return this.finesPerCapita;
	}
	
	public void addZipCode(ZipCode zipCodeObj){
		if(this.zipCodesMap == null) {
			this.zipCodesMap = new HashMap<>();
		}
		
		zipCodesMap.put(zipCodeObj.getZipCode(), zipCodeObj);
	}
}