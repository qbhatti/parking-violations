package edu.upenn.cit594.ui;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.CityProcessor;

public class CommandLineUserInterface {

	private CityProcessor cityProcessor;
	private Logger logger;
	private Scanner scanner;

	public CommandLineUserInterface(CityProcessor cityProcessor, Logger logger) {
		this.cityProcessor = cityProcessor;
		this.logger = logger;
		this.scanner = new Scanner(System.in);
	}

	public void start() {
		boolean exit = false;

		int choice = 0;

		while (!exit) {
			System.out.println("Select from one of the following options. ");
			System.out.println("0 if you would like to exit.");
			System.out.println("1 if you would like to see the total population for all ZIP codes.");
			System.out.println("2 if you would like to see the total parking fees per capita for each ZIP code.");
			System.out.println(
					"3 if you would like to see the average market value for residences in a specified ZIP code.");
			System.out.println(
					"4 if you would like to see the average total livable area for residences in a specified ZIP code.");
			System.out.println(
					"5 if you would like to see the total residential market value per capita for a specified ZIP code.");
			System.out.println(
					"6 if you would like to see the the ratio of total fines to property market value per capita in a specified ZIP code.");
			System.out.println("Enter your selection: ");

			try {
				String inputStr = scanner.nextLine();
				updateLogger(inputStr);
				choice = Integer.parseInt(inputStr);
			} catch (Exception e) {
				System.out.println("Invalid input. You must enter an integer between 0-6.");
				break;
			}

			switch (choice) {
			case 0:
				exit = true;
				break;
			case 1:
				displayTotalPopulation();
				break;
			case 2:
				displayFinesPerCapitaForAllZipCodes();
				break;
			case 3:
				displayAverageMarketValueInZipCode();
				break;
			case 4:
				displayAverageLivableAreaInZipCode();
				break;
			case 5:
				displayMarketValuePerCapitaInZipCode();
				break;
			case 6:
				dispalyTotalFinesToMarketValuePerCapitaRatioInZipCode();
				break;
			default:
				System.out.println("Invalid input. You must enter an integer between 0-6.");
				exit = true;
				break;
			}
		}

		try {
			this.scanner.close();
			this.logger.close();
		} catch (Exception e) {
		}
	}

	// case 1
	private void displayTotalPopulation() {
		System.out.println(this.cityProcessor.calculateTotalPopulation());
	}

	// case 2
	private void displayFinesPerCapitaForAllZipCodes() {
		Map<String, Double> finesPerCapitaMap = cityProcessor.calculateFinesPerCapita();
		for (Entry<String, Double> entry : finesPerCapitaMap.entrySet()) {
			String zipCode = entry.getKey();

			double finePerCapita = Math.floor(entry.getValue() * 10000) / 10000;

			System.out.println(String.format("%s %.4f", zipCode, finePerCapita));
		}
	}

	// case 3
	private void displayAverageMarketValueInZipCode() {
		String zipCode = getZipCodeFromUser();

		// if zipcode input invalid, print '0' and return;
		if (zipCode == null) {
			System.out.println("0");
			return;
		}

		int averageMarketValue = (int) cityProcessor.calculateAverageMarketValue(zipCode.trim());
		System.out.println(averageMarketValue);
	}

	// case 4
	private void displayAverageLivableAreaInZipCode() {
		String zipCode = getZipCodeFromUser();

		// if zipcode input invalid, print '0' and return;
		if (zipCode == null) {
			System.out.println("0");
			return;
		}

		int averageLivableArea = (int) cityProcessor.calculateAverageLivableArea(zipCode);
		System.out.println(averageLivableArea);
	}

	// case 5
	private void displayMarketValuePerCapitaInZipCode() {
		String zipCode = getZipCodeFromUser();

		// if zipcode input invalid, print '0' and return;
		if (zipCode == null) {
			System.out.println("0");
			return;
		}

		int marketValPerCapita = (int) cityProcessor.calculateValuePerCapita(zipCode);
		System.out.println(marketValPerCapita);
	}

	// case 6
	private void dispalyTotalFinesToMarketValuePerCapitaRatioInZipCode() {
		String zipCode = getZipCodeFromUser();

		// if zipcode input invalid, print '0' and return;
		if (zipCode == null) {
			System.out.println("0");
			return;
		}
		
		double ratio = cityProcessor.calculateTotalFinesToMarketValuePerCapitaRatioInZipCode(zipCode);
		System.out.println(String.format("%.2f", ratio));
	}

	// Helper methods

	private String getZipCodeFromUser() {
		System.out.println("Enter Zip Code: ");
		String zipCode = this.scanner.nextLine();

		updateLogger(zipCode);

		if (zipCode.length() > 5) {
			zipCode = zipCode.substring(0, 5);
		}

		if (zipCode.length() < 5 || !zipCode.matches("\\d{5}")) {
			return null;
		}

		return zipCode;
	}

	private void updateLogger(String zipCode) {
		this.logger.write(zipCode);
	}
}
