package edu.upenn.cit594.processor;

import edu.upenn.cit594.data.Property;
import edu.upenn.cit594.data.ZipCode;

public class LivableAreaAverage implements PropertyAverageStrategy {

	@Override
	public String getVal(Property property) {
		return property.getLivableArea();
	}

	@Override
	public void memoizeAverageVal(ZipCode zipCodeObj, double average) {
		zipCodeObj.setAverageLivableArea(average);
		
	}

	@Override
	public void memoizeSumOfValues(ZipCode zipCodeObj, double sum) {
		zipCodeObj.setTotalLivableArea(sum);
		
	}

	@Override
	public double retrieveMemoizedAverageVal(ZipCode zipCodeObj) {
		return zipCodeObj.getAverageLivableArea();
	}	
}
