package edu.upenn.cit594.processor;

import java.util.ArrayList;

import edu.upenn.cit594.data.Property;

public class AverageAreaCalculator implements PropertyAverageCalculator {

	public double calculateAverageProperty(ArrayList<Property> properties) {
		double average = 0;
		if (properties != null) {
			double totalArea = 0;
			int numProperties = 0;
			for (Property property : properties) {
				try {
					// this throws exception if value string is non-numeric or null
					double propertyValue = Double.parseDouble(property.getLivableArea().trim());
					totalArea += propertyValue;
					numProperties++;

				} catch (Exception e) {
				} // ignore any property value that throw exception
			}
			average = totalArea / numProperties;
		}
		
		return average;
		
	}
}
