package edu.upenn.cit594;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import edu.upenn.cit594.data.City;
import edu.upenn.cit594.datamanagement.ParkingReader;
import edu.upenn.cit594.datamanagement.PopulationReader;
import edu.upenn.cit594.datamanagement.PropertiesReader;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.CityProcessor;
import edu.upenn.cit594.ui.CommandLineUserInterface;


public class Main {

		private static final Set<String> FORMATS = new HashSet<>(Arrays.asList("csv", "json"));

		public static void main(String[] args) {

			// Arguments validity checks
			if (args.length != 5) {
				System.out.println(
						"Error: Invalid Arguments. (Format: parking_file_format parking_filename properties_filename population_filename log_filename)");
				return;
			} else if (!FORMATS.contains(args[0])) {
				System.out.println("Error: Invalid format. Format can either be \"json\" or \"csv\".");
				return;
			} else if (!args[1].toLowerCase().endsWith(args[0].toLowerCase())) {
				// if parking filename and format differ
				System.out.println(
						String.format("Error: mismatched format and filename. Filename must end with '.%s'.", args[0]) );
				return;
			} else { // check if all three input files exist and are readable
				for (int i = 1; i <= 3; i++) {
					if (!isValidFile(args[i])) {
						return;
					}
				}
			}

			// extract args
			String parkingFormat = args[0];
			String parkingFilename = args[1];
			String propertiesFilename = args[2];
			String populationFilename = args[3];
			String logFilename = args[4];

			Logger.setFilename(logFilename);
			Logger logger = Logger.getInstance();

			logger.write(String.format("%s %s %s %s %s", parkingFormat, parkingFilename, propertiesFilename,
					populationFilename, logFilename));

			City philadelphia = new City("Philadelphia", "PA");
			
			ParkingReader parkingReader = ParkingReader.createWithGivenFormat(parkingFormat, parkingFilename, logger);
			PopulationReader populationReader = new PopulationReader(populationFilename, logger);
			PropertiesReader propertiesReader = new PropertiesReader(propertiesFilename, logger);
			
			CityProcessor phillyProcessor = new CityProcessor(philadelphia, parkingReader, populationReader, propertiesReader);
			
			CommandLineUserInterface ui = new CommandLineUserInterface(phillyProcessor, logger);
			
			ui.start();
		}

		private static boolean isValidFile(String filename) {
			if (filename == null || filename.isEmpty())
				return false;

			File file = new File(filename);

			if (!file.exists()) {
				System.out.println(String.format("%s does not exist", filename));
				return false;
			} else if (!file.canRead()) {
				System.out.println(String.format("Cannot read %s.", filename));
				return false;
			}
			
			return true;
		}
	}

