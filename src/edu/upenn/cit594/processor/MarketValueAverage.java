package edu.upenn.cit594.processor;

import edu.upenn.cit594.data.Property;
import edu.upenn.cit594.data.ZipCode;

public class MarketValueAverage implements PropertyAverageStrategy{

	@Override
	public String getVal(Property property) {
		return property.getValue();
	}

	@Override
	public void memoizeAverageVal(ZipCode zipCodeObj, double average) {
		zipCodeObj.setAveragePropertyValue(average);
		
	}

	@Override
	public void memoizeSumOfValues(ZipCode zipCodeObj, double sum) {
		zipCodeObj.setTotalPropertyValue(sum);
	}

	@Override
	public double retrieveMemoizedAverageVal(ZipCode zipCodeObj) {
		return zipCodeObj.getAveragePropertyValue();
	}	
}
