package edu.upenn.cit594.processor;

import java.util.ArrayList;

import edu.upenn.cit594.data.Property;

public class AverageValueCalculator implements PropertyAverageCalculator{

	public double calculateAverageProperty(ArrayList<Property> properties) {
		double average = 0;
		if (properties != null) {
			double totalValue = 0;
			int numProperties = 0;
			for (Property property : properties) {
				try {
					// this throws exception if value string is non-numeric or null
					double propertyValue = Double.parseDouble(property.getValue().trim());
					totalValue += propertyValue;
					numProperties++;

				} catch (Exception e) {
				} // ignore any property value that throw exception
			}
			average = totalValue / numProperties;
		}
		
		return average;
	}

}
