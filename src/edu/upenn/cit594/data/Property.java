package edu.upenn.cit594.data;

public class Property {
	private String zipCode;
	private String livableArea;
	private String value;
	
	public Property(String zipCode, String livableArea, String value) {
		this.zipCode = zipCode;
		this.livableArea = livableArea;
		this.value = value;
	}
	
	public String getZipCode() {
		return zipCode;
	}
	
	public String getLivableArea() {
		return livableArea;
	}
	
	public String getValue() {
		return value;
	}
}
