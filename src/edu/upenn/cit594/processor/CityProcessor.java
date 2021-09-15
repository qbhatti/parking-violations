package edu.upenn.cit594.processor;

import java.util.Deque;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import edu.upenn.cit594.data.City;
import edu.upenn.cit594.data.ParkingViolation;
import edu.upenn.cit594.data.Property;
import edu.upenn.cit594.data.ZipCode;
import edu.upenn.cit594.datamanagement.ParkingReader;
import edu.upenn.cit594.datamanagement.PopulationReader;
import edu.upenn.cit594.datamanagement.PropertiesReader;

public class CityProcessor {
	// Readers
	private ParkingReader parkingReader;
	private PopulationReader populationReader;
	private PropertiesReader propertiesReader;

	private City city;

	public CityProcessor(City city, ParkingReader parkingReader, PopulationReader populationReader,
			PropertiesReader propertiesReader) {

		this.parkingReader = parkingReader;
		this.populationReader = populationReader;
		this.propertiesReader = propertiesReader;
		this.city = city;
	}

	// Option 1
	public int calculateTotalPopulation() {
		
		this.lazyLoadData(true, false, false);
		
		int cityPopulation = city.getTotalPopulation();

		if (cityPopulation > 0) {
			return cityPopulation;
		} 

		Map<String, ZipCode> zipCodesMap = city.getZipCodesMap();

		cityPopulation = 0;

		for (ZipCode zipCode : zipCodesMap.values()) {
			cityPopulation += zipCode.getPopulation();
		}

		// memoize
		this.city.setTotalPopulation(cityPopulation);

		return cityPopulation;
	}

	// Option 2
	public Map<String, Double> calculateFinesPerCapita() {
		this.lazyLoadData(true, true, false);
		
		Map<String, Double> finesPerCapitaMap = this.city.getFinesPerCapita();
		
		if (finesPerCapitaMap != null) {
			return finesPerCapitaMap;
		}

		Map<String, ZipCode> zipCodesMap = this.city.getZipCodesMap();

		finesPerCapitaMap = new TreeMap<>();

		// for each zip code, add up fine from all the violations, divide by population
		// and add to finesPerCapitaMap
		for (Entry<String, ZipCode> entry : zipCodesMap.entrySet()) {
			ZipCode currentZipCode = entry.getValue();
			int population = currentZipCode.getPopulation();
			double totalFines = calculateTotalFinesInZipCode(currentZipCode);

			if (population > 0 && totalFines > 0) {
				finesPerCapitaMap.put(entry.getKey(), (totalFines / population));
			}
		}

		// memoize
		this.city.setFinesPerCapita(finesPerCapitaMap);

		return finesPerCapitaMap;
	}

	// option 3
	public double calculateAverageMarketValue(String zipCode) {
		return calculatePropertyAverage(zipCode, new MarketValueAverage());
	}

	// option 4
	public double calculateAverageLivableArea(String zipCode) {
		return calculatePropertyAverage(zipCode, new LivableAreaAverage());
	}

	private double calculatePropertyAverage(String zipCode, PropertyAverageStrategy strategy) {
		//load population and then properties data if not already done
		this.lazyLoadData(true, false, true);
		
		ZipCode zipCodeObj = this.city.getZipCode(zipCode);

		// if zipCode is not valid
		if (zipCodeObj == null) {
			return 0;
		}

		double average = strategy.retrieveMemoizedAverageVal(zipCodeObj);

		// if average hasn't yet been memoized, calculate it.
		if (average < 0) {
			average = 0;
			double totalValue = 0;
			int numPropertiesInTotal = 0;

			Deque<Property> properties = zipCodeObj.getProperties();

			for (Property property : properties) {
				String strValue = strategy.getVal(property);
				try {
					totalValue += Double.parseDouble(strValue);
					numPropertiesInTotal++;
				} catch (Exception e) {} // ignore exception and read next value
			}

			average = totalValue / numPropertiesInTotal;

			// memoize
			strategy.memoizeAverageVal(zipCodeObj, average);
			strategy.memoizeSumOfValues(zipCodeObj, totalValue);
		}

		return average;
	}
	
	//option 5
	public double calculateValuePerCapita(String zipCode) {
		this.lazyLoadData(true, false, true);
		
		double valuePerCapita = 0;
		ZipCode zipCodeObj = this.city.getZipCode(zipCode);
		
		if(zipCodeObj != null) {
			valuePerCapita = zipCodeObj.getValuePerCapita();
			
			//if valuePerCapita isn't already memoized
			if(valuePerCapita < 0) {
				// determine total value
				double totalValue = zipCodeObj.getTotalPropertyValue();
				
				//if total value isn't already memoized
				if(totalValue < 0) {
					Deque<Property> properties = zipCodeObj.getProperties();
					
					totalValue = 0;
					for(Property property: properties) {
						try {
							String strValue = property.getValue().trim();
							totalValue += Double.parseDouble(strValue);
						} catch (Exception e) {} // ignore exception and read next value
					}
				}
				int population = zipCodeObj.getPopulation();
				
				if(population > 0 && totalValue > 0) {
					valuePerCapita = totalValue / population;
				} else {
					valuePerCapita = 0;
				}
				
				//memoize
				zipCodeObj.setValuePerCapita(valuePerCapita);
			}
		}
		
		return valuePerCapita;
	}
	
	//case 6
	public double calculateTotalFinesToMarketValuePerCapitaRatioInZipCode(String zipCode) {
		this.lazyLoadData(true, true, true);
		
		double ratio = 0;
		
		ZipCode zipCodeObj = this.city.getZipCode(zipCode);
		
		if(zipCodeObj != null) {
			//determine totalFines
			double totalFines = calculateTotalFinesInZipCode(zipCodeObj);
			
			//determine market value per capita
			double marketValuePerCapita = calculateValuePerCapita(zipCode);
			
			if(marketValuePerCapita > 0) {
				ratio = totalFines / marketValuePerCapita;
			}
		}
		
		return ratio;
	}
	
	
	//Helper methods
	
	private double calculateTotalFinesInZipCode(ZipCode zipCodeObj) {
		double totalFines = zipCodeObj.getTotalFines();
		if(totalFines < 0) {
			totalFines = 0;
			Deque<ParkingViolation> violations = zipCodeObj.getParkingViolations();
			
			for (ParkingViolation violation : violations) {
				totalFines += violation.getFineAmount();
			}
			
			// memoize
			zipCodeObj.setTotalFines(totalFines);
		}
		
		return totalFines;
	}
	
	private void lazyLoadData(boolean loadPopulationData, boolean loadParkingData, boolean loadPropertiesData) {
		if(loadPopulationData) { // population data must be read before everything else.
			if (!this.city.getIsPopulationDataAvailable()) {
				this.populationReader.readCityPopulationData(this.city);
				this.city.setIsPopulationDataAvailable(true);
			}
		}
		
		if(loadParkingData) {
			if (!this.city.getIsParkingDataAvailable()) {
				this.parkingReader.readParkingData(this.city);
				this.city.setIsParkingDataAvailable(true);
			}
		}
		
		if(loadPropertiesData) {
			if (!this.city.getIsPropertyDataAvailable()) {
				this.propertiesReader.readPropertiesData(this.city);
				this.city.setIsPropertyDataAvailable(true);
			}
		}
	}
}
