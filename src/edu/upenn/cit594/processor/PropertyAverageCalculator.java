package edu.upenn.cit594.processor;

import java.util.ArrayList;

import edu.upenn.cit594.data.Property;

public interface PropertyAverageCalculator {
	public double calculateAverageProperty(ArrayList<Property> properties);
}
