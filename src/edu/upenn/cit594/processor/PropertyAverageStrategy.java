package edu.upenn.cit594.processor;
import edu.upenn.cit594.data.Property;
import edu.upenn.cit594.data.ZipCode;

public interface PropertyAverageStrategy {
	public String getVal(Property property);
	public void memoizeAverageVal(ZipCode zipCodeObj, double average);
	public void memoizeSumOfValues(ZipCode zipCodeObj, double sum);
	public double retrieveMemoizedAverageVal(ZipCode zipCodeObj);
	
}
